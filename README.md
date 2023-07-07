## Micronaut 3.9.3 Documentation

- [User Guide](https://docs.micronaut.io/3.9.3/guide/index.html)
- [API Reference](https://docs.micronaut.io/3.9.3/api/index.html)
- [Configuration Reference](https://docs.micronaut.io/3.9.3/guide/configurationreference.html)
- [Micronaut Guides](https://guides.micronaut.io/index.html)
---

- [Micronaut Gradle Plugin documentation](https://micronaut-projects.github.io/micronaut-gradle-plugin/latest/)
- [GraalVM Gradle Plugin documentation](https://graalvm.github.io/native-build-tools/latest/gradle-plugin.html)
- [Shadow Gradle Plugin](https://plugins.gradle.org/plugin/com.github.johnrengelman.shadow)
## Feature serialization-jackson documentation

- [Micronaut Serialization Jackson Core documentation](https://micronaut-projects.github.io/micronaut-serialization/latest/guide/)


## Feature serialization-jsonp documentation

- [Micronaut Serialization JSON-B and JSON-P documentation](https://micronaut-projects.github.io/micronaut-serialization/latest/guide/)


## Feature openapi documentation

- [Micronaut OpenAPI Support documentation](https://micronaut-projects.github.io/micronaut-openapi/latest/guide/index.html)

- [https://www.openapis.org](https://www.openapis.org)


## Feature swagger-ui documentation

- [Micronaut Swagger UI documentation](https://micronaut-projects.github.io/micronaut-openapi/latest/guide/index.html)

- [https://swagger.io/tools/swagger-ui/](https://swagger.io/tools/swagger-ui/)


## Feature http-client documentation

- [Micronaut HTTP Client documentation](https://docs.micronaut.io/latest/guide/index.html#httpClient)


## Feature cache-caffeine documentation

- [Micronaut Caffeine Cache documentation](https://micronaut-projects.github.io/micronaut-cache/latest/guide/index.html)

- [https://github.com/ben-manes/caffeine](https://github.com/ben-manes/caffeine)


start 1




S - X - X - X - X - X - X - X - X - X - X - X - F
1 x 4 x 4 x 4 x 4 x 4 x 4 x 4 x 4 x 4 x 4 x 4 x 1 



zobrazi zbezne mapy s CP zoradene podla resources 


-> detail mapy -> finetuning
   -> zobrazi pocasie
   -> zobrazi benzinky popri ceste s moznostou pridania na trasu



   -> pocet najblizsich a celkovy pocet musia byt v rovnovahe

 - naj / cp
 - 3 / 14
 - 10 / 3


- rest each 70km

 - CP 1 CP 2 

 after 0 -> possible REST stations



 - vyhladam cesty
 - vyberiem si oblubene (planning id + routeIdx)
 - pre vybranu cestu -> route detail (planning id + routeIdx)
   - zobrazi pocasie a moznost pridat k oblubenym
   - zobrazi dostupne restpoint 
   - moznost vybrat restpoints
   - vyg
 - 

 /plannings/ID/routes/IDX/gpx
 /plannings/ID/routes/IDX/weather?withRestpoints

 -davat len posledny s poslednym.....

 
 VYSKOVE DATA - https://zbgis.skgeodesy.sk/geoportal/catalog/search/resource/details.page?uuid=%7B24D07BC1-29CC-445A-814A-19B58FF29FA9%7D


 http://my-wcs-server.com/wcs?service=WCS&version=2.0.1&request=GetCoverage&coverageId=myCoverage&format=application/geotiff

https://zbgisws.skgeodesy.sk/inspire_elevation_wcs/service.svc/get?service=WCS&version=2.0.0&request=GetCoverage&coverageId=myCoverage&format=application/geotiff


## Backlog - Backend
- pumpa
- navleky
- rukavicky
- krem
- duse
- sada na opravu plasta + lepidlo
- montpaky backup 1 

### MUST: Bike profile
As an ultrabiker,
I want the route to planned via suitable roads,
So I don't end un on unapropriate road type.

### MUST: Checkpoint matrix caching
As an ultrabiker,
I want matrix between checkpoints to be cached,
so I can get available routes as soon as possible.

### MUST: User specific search 
As an ultrabiker,
I want to see only my searchers,
so I don't need to go through searches of others.

### MUST: Favourite route search results
As an ultrabiker who triggered several searcher,
I want to favourite those I like,
so I can get back to them in easy way.

* Mark search as favourite
* Unfavourite search

### MUST: Rename route search
As an ultrabiker

* Default name generation
* Rename search

### MUST: Persistant search
* File DB instead memory

### MUST: Create tour from found route

* POST /tours
* GET
* GET /tours/{id}
### MUST: Download tour GPX 
As an ultrabiker who prepared final tour,
I want to download it in GPX format (or other format),
so I can load the tour to my bike computer. 

### MUST: Get tour details with avaliable restpoints
As an ultrabiker,
I want to get available restpoins between CPs with minimal penalty,
so I can include them in my tour.

* Reststations: Slovnaft, Shell, OMV
* 3 restpoints with minimal penalty

### MUST: Select tour restpoints


### MUST: Create empty planning from KML
As an ultrabiker who would like to start planning route,
I want a default planning with give checkpoints to start from,
so I don't need to parse and add checkpoints manually.

* Input: KML file
* Output: checkpoints: []

### SHOULD: Download splitted GPX



### SHOULD: Optimize for less checkpoints

* Relation between count(nereast) amd noOfCheckpoints
* Max nereast circle according length.....

### SHOULD: Accurate elevation data 

### SHOULD: Ordering routes

### SHOULD: Persistant fast DB

## Backlog - UI

## Backlog - OPS

##