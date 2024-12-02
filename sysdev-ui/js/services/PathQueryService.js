/* global kangaroute */

sysdevinterface.service('pathQueryService', [ '$http', 'modifyMap', function ($http, modifyMap) {
  let that = this
  const baseUrl = 'http://localhost:9090/sysdev'
  const key = "5b3ce3597851110001cf6248aa5a2f545e11431d948fc5566c0d1187";

  /* mocks the requesting of algorithm data from the server and saves the response to the scope */
  this.getInitialInformation = function (model) {
    $http.get('models/algorithms.json')
      .then(response => {
        model['algorithms'] = response.data.Path_Queries
        model.selected['algorithm'] = model.algorithms[0]
        //model.selected['costs'] = [model.selected.algorithm.criteria[0]]
      })
  }

  /* fetchDirect builds the request JSON, sends it to the server and handles the response */
  this.fetchDirect = function (model) {
    let criteria_map = new Object();
    /*
    for(let j = 0; j < model.selected.costs.length; j++){
        criteria_map[model.selected.costs[j]] = 1.0
    }
    let criteria_list = model.selected.costs;
    */
    let markers = model.map.markers
    let requestObject = {
      's': {
        'lat': markers[0].lat,
        'lon': markers[0].lng
      },
      't': {
        'lat': markers[1].lat,
        'lon': markers[1].lng
      },
      //'name': model.selected.algorithm.internal_name,
      '@class': model.selected.algorithm.class,
      //'criteria': criteria_map
    }
    model['usedAlgorithm'] = model.selected.algorithm.internal_name
      switch(model['usedAlgorithm']){
        case 'Openrouteservice (direct)':
          url = "https://api.openrouteservice.org/v2/directions/driving-car?api_key=" + key
                    +"&start="+markers[0].lng+","+markers[0].lat+"&end="+markers[1].lng+","+markers[1].lat;
          $http.get(url).success(function(response) {
            geojson = {'data': response}; // that.extractGeoJson(response);
            that.routeResponse(model, geojson);
          });
          break;
        case 'Openrouteservice (via Server [GET])':
          url = baseUrl+"/orsdirections?"+"originLat="+markers[0].lat+"&originLon="+markers[0].lng+"&destinationLat="+markers[1].lat+"&destinationLon="+markers[1].lng;
          $http.get(url).success(function(response) {
            geojson = {'data': response}; // that.extractGeoJson(response);
            that.routeResponse(model, geojson);
          });
          break;
        case 'Openrouteservice (via Server [POST])':
          var request = {
            "originLat": markers[0].lat,
            "originLon": markers[0].lng,
            "destinationLat": markers[1].lat,
            "destinationLon": markers[1].lng
          };
          $http.post(baseUrl+"/orsdirections", request)
          .then(response => {
                geojson = response; // {'data': response}; // that.extractGeoJson(response);
                that.routeResponse(model, geojson);
              })
          break;
        case 'Dijkstra Shortest Path':
          url = baseUrl+"/dijkstra?"+"originLat="+markers[0].lat+"&originLon="+markers[0].lng+"&destinationLat="+markers[1].lat+"&destinationLon="+markers[1].lng;
          $http.get(url).success(function(response) {
            geojson = {'data': response}; // that.extractGeoJson(response);
            that.routeResponse(model, geojson);
          });
          break;
        case 'A* Shortest Path':
          url = baseUrl+"/astar?"+"originLat="+markers[0].lat+"&originLon="+markers[0].lng+"&destinationLat="+markers[1].lat+"&destinationLon="+markers[1].lng;
          $http.get(url).success(function(response) {
            geojson = {'data': response}; // that.extractGeoJson(response);
            that.routeResponse(model, geojson);
          });
          break;
      }
  }

  this.extractGeoJson = function (response) {
    points = google.maps.geometry.encoding.decodePath(response.routes[0].overview_polyline.points)
    var json = {
        "type": "FeatureCollection",
            "features": []
    };
    var polylineFeature = {
        "type": "Feature",
            "geometry": {
            "type": "LineString",
                "coordinates": []
        },
            "properties": {}
    };
    for (var i = 0; i < points.length; i++) {
        var pt = points[i];
        polylineFeature.geometry.coordinates.push([
        pt.lng(), pt.lat()]);
    }
    polylineFeature.properties.costs = {};
    polylineFeature.properties.costs.Distance = (response.routes[0].legs[0].distance.value / 1000);
    polylineFeature.properties.costs.Travel_Time = (response.routes[0].legs[0].duration.value / 60);
    polylineFeature.properties.instructions = []
    json.features.push(polylineFeature);
    var wrapper = {};
    wrapper.data = json;
    return wrapper;
  }

  /* add route to map of a single route algorithm and set route instructions to the scope */
  this.routeResponse = function (model, geojson) {
     model['algorithmKind'] = 'single'
     //geojson.data.features[0].properties.instructions = [model.map.markers[0].formattedAddress[0], model.map.markers[1].formattedAddress[0]]
     geojson.data.features[0].properties['index'] = 0
     model.map.geojson = geojson
     model.map.geojson['style'] = {
       opacity: 1
     }
     //model.map['routeInfo'] = geojson.data.features[0].properties.costs
     modifyMap.centerOnRoute(model)
  }

  /* removes route from the map and deletes used algorithm and route info */
  this.removeRoute = function (model) {
    model.map.geojson = []
    model.usedAlgorithm = undefined
    model.map.routeInfo = undefined
  }

  /* handles the selection of the algorithm */
  this.selectAlgo = function (model, algorithm) {
    model.selected.algorithm = algorithm
    /*
    model.selected.costs = []
    for(let j = 0; j < model.selected.algorithm.min_criteria; j++){
        model.selected.costs.push(model.selected.algorithm.criteria[j])
    }
    */
  }

  /* handles the selection of the algorithms costs */
  /*
  this.selectCost = function (model, cost) {
    let selected = model.selected
    if (selected.algorithm.max_criteria === 1) selected.costs = [cost]
    else {
      if (selected.costs.indexOf(cost) === -1) selected.costs.push(cost)
      else selected.costs.splice(selected.costs.indexOf(cost), 1)
    }
  }
  */
}])
