package cz.krvotok.tdf.adapter.out.persistance.kml;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import cz.krvotok.tdf.domain.model.aggregate.TourDeFelvidek;
import cz.krvotok.tdf.domain.model.valueobject.Checkpoint;
import cz.krvotok.tdf.domain.repository.TourDeFelvidekRepository;
import cz.krvotok.tdf.application.service.NavigationService;
import jakarta.inject.Singleton;

@Singleton
public class TourDeFelvidekRepositoryImpl implements TourDeFelvidekRepository {

    private final NavigationService navigationService;

    public TourDeFelvidekRepositoryImpl(NavigationService navigationService) {
        this.navigationService = navigationService;
    }

    @Override
    public Optional<TourDeFelvidek> findByYear(int year) {
        TourDeFelvidek tdf = new TourDeFelvidek();
        tdf.setYear(year);
        List<Checkpoint> checkpoints = new ArrayList<>();

        try {
            String tdfFilePath = Paths.get("src/main/resources/kml/tdf-" + year + ".kml").toAbsolutePath().toString();
            File tdfFile = new File(tdfFilePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(tdfFile);
            doc.getDocumentElement().normalize();
            NodeList checkpointNodes = doc.getElementsByTagName("Placemark");

            for (int i = 0; i < checkpointNodes.getLength(); i++) {
                Node checkpointNode = checkpointNodes.item(i);
                Element checkpointElement = (Element) checkpointNode;

                String name = checkpointElement.getElementsByTagName("name").item(0).getTextContent();

                String description;
                if (checkpointElement.getElementsByTagName("description").getLength() == 1) {
                    description = checkpointElement.getElementsByTagName("description").item(0).getTextContent();

                } else {
                    description = "";
                }
                String coordinates = checkpointElement.getElementsByTagName("coordinates").item(0).getTextContent();
                String[] splitCoordinates = coordinates.split(",");
                String longitude = splitCoordinates[0];
                String latitude = splitCoordinates[1];

                Checkpoint checkpoint = new Checkpoint();
                checkpoint.setName(name);
                checkpoint.setDescription(description);
                checkpoint.setAltitude(this.navigationService.getPointAltitude(Double.parseDouble(latitude), Double.parseDouble(longitude)));
                checkpoint.setLatitude(Double.parseDouble(latitude));
                checkpoint.setLongitude(Double.parseDouble(longitude));

                if (description.contains("Start") || description.contains("START")) {
                    tdf.setStartCheckpointIdx(i);
                }

                if (description.contains("Finish") || description.contains("FINISH")) {
                    tdf.setFinishCheckpointIdx(i);
                }

                checkpoints.add(checkpoint);
            }
        } catch (IOException | NumberFormatException | ParserConfigurationException | DOMException | SAXException e) {
            return Optional.ofNullable(null);
        }

        tdf.setCheckpoints(checkpoints);

        return Optional.of(tdf);
    }
}