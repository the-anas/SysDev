/* global kangaroute */

/**
 * all constants are declared in config-service
 **/

sysdevinterface.factory('config', function () {
  let common = {
    subdomains: 'abc',
    app_id: 'WmIkt7vA4CQCMLSXEmOf',
    app_code: 'LBj3S0_CED-_JWWO4VvUcg',
    mapID: 'newest',
    maxZoom: 18,
    language: 'eng',
    format: 'png8',
    size: '256',
    showOnSelector: false
  }
  let map = {
    name: 'Map',
    url: 'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
    type: 'xyz',
    layerOptions: Object.assign({
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
    }, common)
  }
  let satellite = {
    name: 'Map',
    url: 'http://tile.memomaps.de/tilegen/{z}/{x}/{y}.png',
    type: 'xyz',
    layerOptions: Object.assign({
        attribution: '&copy; <a href="http://öpnvkarte.de">Öpnvkarte</a> contributors'
    }, common)
  }
  let poi = {
    'eat-drink': 'Eat & Drink',
    'going-out': 'Going Out',
    'sights-museums': 'Sights & Museums',
    'transport': 'Transportation',
    'shopping': 'Shopping',
    'leisure-outdoor': 'Leisure & Outdoor',
    'accommodation,administrative-areas-buildings,natural-geographical,petrol-station,atm-bank-exchange,toilet-rest-area,hospital-health-care-facility': 'Other'
  }
  return {
    model: {
      map: {
        center: {
          lat: 52.512,
          lng: 13.401,
          zoom: 12
        },
        defaults: {
          maxZoom: 18,
          minZoom: 5
        },
        layers: {
        },
        baselayers: {
          map: map,
          satellite: satellite
        },
        geojson: [],
        markers: [],
        paths: []
      },
      simulation: {},
      poi: poi,
      selected: {
        poi: []
      }
    }
  }
})
