package cz.krvotok.tdf.application.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import cz.krvotok.tdf.domain.model.valueobject.Checkpoint;
import cz.krvotok.tdf.domain.model.aggregate.Search;
import cz.krvotok.tdf.domain.model.valueobject.Route;
import cz.krvotok.tdf.domain.repository.SearchRepository;
import io.micronaut.scheduling.annotation.Async;
import jakarta.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Singleton
public class RouteSearcher {
    private static final Logger LOG = LoggerFactory.getLogger(RouteSearcher.class);

    class CheckpointsMatrix {
        public int[][] distanceMatrix;
        public int[][] ascendMatrix;
        public int[][] descendMatrix;
        public int[][] nearestIdxsMatrix;
    }

    private final PathMetadataFinder pathMetadataFinder;
    private final SearchRepository searchRepository;
    private int stat;

    public RouteSearcher(SearchRepository searchRepository, PathMetadataFinder pathMetadataFinder) {
        this.searchRepository = searchRepository;
        this.pathMetadataFinder = pathMetadataFinder;
    }

    @Async
    public void searchForRoutes(UUID searchId) {
        Search search = this.searchRepository.findById(searchId).orElse(null);
        
        // Update status.
        search.setStatus(Search.STATUS_CALCULATING);
        this.searchRepository.persist(search);
        
        // Calculate checkpoints matrix.
        CheckpointsMatrix checkpointsMatrix = this.calculateCheckpointsMatrix(
            search.getCheckpoints(), 
            search.getFinishCheckpointIdx(),
            search.getMaxDistance() 
        );

        // Initialize first route.
        int[] routeCheckpoints = new int[search.getNoOfCheckpoints() + 2];// + start and finish
        Arrays.fill(routeCheckpoints, -1);

        this.stat = 0; 
        calculateRoute(
            search.getStartCheckpointIdx(),
            -1, 
            routeCheckpoints, 
            0, 
            0,
            search, 
            checkpointsMatrix
        );

        // Update status.
        search.setStatus(Search.STATUS_READY);
        this.searchRepository.persist(search);

        System.out.println(this.stat);
    }

    private void calculateRoute(
        int nextCheckpointIdx,
        int lastCheckpointIdx,
        int[] routeCheckpointsIdxs,
        int distance,
        int ascend,
        Search search,
        CheckpointsMatrix checkpointsMatrix
    ) {
        this.stat = this.stat + 1;
        System.out.println(Arrays.toString(routeCheckpointsIdxs));
        int currentRouteCheckpointIdx = -1;
        int remainingRouteCheckpoints = -1;

        // Identify where we are in the route and how many CP remains.
        for (int i = 0; i < routeCheckpointsIdxs.length; i++) {
            if (routeCheckpointsIdxs[i] == -1) {
                currentRouteCheckpointIdx = i;
                remainingRouteCheckpoints = routeCheckpointsIdxs.length - i;

                break;
            }
        }

        // Optimization: Do not continue if next checkpoint is finish, but we are not yet there.
        if (remainingRouteCheckpoints > 1 && nextCheckpointIdx == search.getFinishCheckpointIdx()) {
            LOG.trace("Skipping route because finish can be only last checkpoint.");
            // + remainingRouteCheckpoints ^ 6
            return;
        }

        // Optimizaion: Do not continue if last checkpoint but not finish.
        if (remainingRouteCheckpoints == 1 && nextCheckpointIdx != search.getFinishCheckpointIdx()) {
            LOG.trace("Skipping route because last checkpoint must be finish.");
            // + 1 skipped
            return;
        }

        // Add next checkpoint to the route.
        routeCheckpointsIdxs[currentRouteCheckpointIdx] = nextCheckpointIdx;

        // Recalculate distance and ascend for the route.
        if (lastCheckpointIdx != -1) {
            distance = distance + checkpointsMatrix.distanceMatrix[lastCheckpointIdx][nextCheckpointIdx];
            ascend = ascend + checkpointsMatrix.ascendMatrix[lastCheckpointIdx][nextCheckpointIdx];
        }

        // Optimization: Do not continue if route exceeds max distance.
        if (distance > search.getMaxDistance()) {
            LOG.trace("Skipping route because max distance exceeded.");
            // + 1 skipped
            return;
        }

        // Optimization: Do not continue if route exceeds max ascend.
        if (ascend > search.getMaxAscend()) {
            LOG.trace("Skipping route because max ascend exceeded.");
            // + 1 skipped
            return;
        }

        // Add route if last checkpoint.
        if (remainingRouteCheckpoints == 1) {
            LOG.info("Found route: " + Arrays.toString(routeCheckpointsIdxs) + " | Distance:" + distance + " | Ascend:" + ascend);
            
            boolean routeAdded = search.addRoute(new Route(routeCheckpointsIdxs, distance, ascend));
            
            // Persist search only if new routa was really added to search.
            if (routeAdded) {
                this.searchRepository.persist(search);
            }

            // + 1 found

            return;
        }


        // Continue the route with nereast checkpoints.
        int[] nearestCheckpointIdxs = checkpointsMatrix.nearestIdxsMatrix[nextCheckpointIdx];

        // Add finish CP (if not there) if the next CP will be last CP of the route.
        if (remainingRouteCheckpoints == 2) {
            nearestCheckpointIdxs = RouteSearcher.addCheckpointIdx(nearestCheckpointIdxs, search.getFinishCheckpointIdx());
        }

        for (int i = 0; i < nearestCheckpointIdxs.length; i++) {
            // Optimization: Skip checkpoints already in the route.
            int z = nearestCheckpointIdxs[i];
            if (Arrays.stream(routeCheckpointsIdxs).anyMatch(j -> j == z)) {
                LOG.trace("Skipping route because checkpoint already in route.");

                // + remaining CP ^ 6
                continue;
            }

            // Skip checkpoint if not defined (for situation when there was less than six available checkpoints).
            if (nearestCheckpointIdxs[i] == -1) {
                LOG.trace("Skipping route because not real checkpoint.");

                // + remaining CP ^ 6

                continue;
            }

            int[] nextRouteCheckpointsIdxs = Arrays.copyOf(routeCheckpointsIdxs, routeCheckpointsIdxs.length);

            calculateRoute(
                nearestCheckpointIdxs[i], // next
                nextCheckpointIdx, // previous
                nextRouteCheckpointsIdxs,
                distance,
                ascend,
                search,
                checkpointsMatrix
            );
        }
    }

    private CheckpointsMatrix calculateCheckpointsMatrix(List<Checkpoint> checkpoints, int finishCheckpointIdx, int maxDistance) {
        CheckpointsMatrix checkpointsMatrix = new CheckpointsMatrix();
        checkpointsMatrix.distanceMatrix = new int[checkpoints.size()][checkpoints.size()];
        checkpointsMatrix.ascendMatrix = new int[checkpoints.size()][checkpoints.size()];
        checkpointsMatrix.descendMatrix = new int[checkpoints.size()][checkpoints.size()];
        checkpointsMatrix.nearestIdxsMatrix = new int[checkpoints.size()][];

        for (int i = 0; i < checkpoints.size(); i++) {
            int firstMinIndex = -1;     int firstMin = Integer.MAX_VALUE;
            int secondMinIndex = -1;    int secondMin = Integer.MAX_VALUE;
            int thirdMinIndex = -1;     int thirdMin = Integer.MAX_VALUE;
            int fourthMinIndex = -1;    int fourthMin = Integer.MAX_VALUE;
            int fifthMinIndex = -1;     int fifthMin = Integer.MAX_VALUE;
            int sixthMinIndex = -1;     int sixthMin = Integer.MAX_VALUE;

            for (int j = 0; j < checkpoints.size(); j++) {
                
                // Optimization: Do not calculate if same points.
                if (j == i) {
                    checkpointsMatrix.distanceMatrix[i][j] = 0;
                    checkpointsMatrix.ascendMatrix[i][j] = 0;
                    checkpointsMatrix.descendMatrix[i][j] = 0;

                    continue;
                }

                // Optimization: Do not calculate if points too far from each other and they are not finish.
                if (j != finishCheckpointIdx && RouteSearcher.haversine(checkpoints.get(i).getLatitude(), checkpoints.get(i).getLongitude(), checkpoints.get(j).getLatitude(), checkpoints.get(j).getLongitude()) > 100) {
                    checkpointsMatrix.distanceMatrix[i][j] = -1;
                    checkpointsMatrix.ascendMatrix[i][j] = -1;
                    checkpointsMatrix.descendMatrix[i][j] = -1;

                    continue;
                }

                // do not calculate if opposite direction already available
                int distance;
                int ascend;
                int descend;

                if (checkpointsMatrix.distanceMatrix[j][i] != 0) {
                    distance = checkpointsMatrix.distanceMatrix[j][i];
                    ascend = checkpointsMatrix.descendMatrix[j][i];
                    descend = checkpointsMatrix.ascendMatrix[j][i];
                } else {
                    int[] path = this.pathMetadataFinder.findPathMetadata(
                        checkpoints.get(i).getLatitude(),
                        checkpoints.get(i).getLongitude(),
                        checkpoints.get(j).getLatitude(),
                        checkpoints.get(j).getLongitude()
                    );

                    distance = (int) path[0];
                    ascend = (int) path[1];
                    descend = (int) path[2];
                }

                checkpointsMatrix.distanceMatrix[i][j] = distance;
                checkpointsMatrix.ascendMatrix[i][j] = ascend;
                checkpointsMatrix.descendMatrix[i][j] = descend;

                if (distance < firstMin) {
                    sixthMin = fifthMin;
                    sixthMinIndex = fifthMinIndex;
                    fifthMin = fourthMin;
                    fifthMinIndex = fourthMinIndex;
                    fourthMin = thirdMin;
                    fourthMinIndex = thirdMinIndex;
                    thirdMin = secondMin;
                    thirdMinIndex = secondMinIndex;
                    secondMin = firstMin;
                    secondMinIndex = firstMinIndex;
                    firstMin = distance;
                    firstMinIndex = j;
                } else if (distance < secondMin) {
                    sixthMin = fifthMin;
                    sixthMinIndex = fifthMinIndex;
                    fifthMin = fourthMin;
                    fifthMinIndex = fourthMinIndex;
                    fourthMin = thirdMin;
                    fourthMinIndex = thirdMinIndex;
                    thirdMin = secondMin;
                    thirdMinIndex = secondMinIndex;
                    secondMin = distance;
                    secondMinIndex = j;
                } else if (distance < thirdMin) {
                    sixthMin = fifthMin;
                    sixthMinIndex = fifthMinIndex;
                    fifthMin = fourthMin;
                    fifthMinIndex = fourthMinIndex;
                    fourthMin = thirdMin;
                    fourthMinIndex = thirdMinIndex;
                    thirdMin = distance;
                    thirdMinIndex = j;
                } else if (distance < fourthMin) {
                    sixthMin = fifthMin;
                    sixthMinIndex = fifthMinIndex;
                    fifthMin = fourthMin;
                    fifthMinIndex = fourthMinIndex;
                    fourthMin = distance;
                    fourthMinIndex = j;
                } else if (distance < fifthMin) {
                    sixthMin = fifthMin;
                    sixthMinIndex = fifthMinIndex;
                    fifthMin = distance;
                    fifthMinIndex = j;
                } else if (distance < sixthMin) {
                    sixthMin = distance;
                    sixthMinIndex = j;
                }
            }

            checkpointsMatrix.nearestIdxsMatrix[i] = new int[]{firstMinIndex, secondMinIndex, thirdMinIndex, fourthMinIndex, fifthMinIndex, sixthMinIndex};

            System.out.println("Checkpoint: " + checkpoints.get(i).getName());
            System.out.println("Distance: " + Arrays.toString(checkpointsMatrix.distanceMatrix[i]));
            System.out.println("Ascend: " + Arrays.toString(checkpointsMatrix.ascendMatrix[i]));
            System.out.println("Descend: " + Arrays.toString(checkpointsMatrix.descendMatrix[i]));
            System.out.println("Nearest Idxs: " + Arrays.toString(checkpointsMatrix.nearestIdxsMatrix[i]));

        }
        
        return checkpointsMatrix;
    }   


    static public double haversine(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.pow(Math.sin(dLat / 2),2) + Math.pow(Math.sin(dLon / 2),2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));

        return 6372.8 * c;
    }

    public static int[] addCheckpointIdx(int[] originalCheckpointIdxs, int checkpointIdx) {
        for (int originalCheckpointIdx : originalCheckpointIdxs) {
            if (originalCheckpointIdx == checkpointIdx) {
                return originalCheckpointIdxs;  // element already exists in the array, so return the original array
            }
        }
        // element doesn't exist in the array, so create a new array with size one greater
        int[] newCheckpointIdxs = new int[originalCheckpointIdxs.length + 1];

        // Copy elements from old array to the new array
        System.arraycopy(originalCheckpointIdxs, 0, newCheckpointIdxs, 0, originalCheckpointIdxs.length);

        // Add the new element to the last index
        newCheckpointIdxs[originalCheckpointIdxs.length] = checkpointIdx;

        return newCheckpointIdxs;
    }
}