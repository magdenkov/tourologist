/**
 * Created by rafael on 28/11/2016.
 */
'use strict';

angular
    .module('tourologistApp').factory('geoHelpers', function () {

    var BASE_URL = '//maps.googleapis.com/maps/api/staticmap?';
    var STYLE_ATTRIBUTES = ['color', 'label', 'size'];

    return {
        getMapsKey: function () {
            return "AIzaSyCr3ZtMml1JIFb1x4Z5mYLlfriotaumJ84";
        },
        getBounds: function (array) {
            //Set bounds to their opposite values to begin, work backwards from here
            var bounds = {
                northeast: {
                    latitude: -90, //Highest
                    longitude: -180 //Highest
                },
                northwest: {
                    latitude: -90, //Highest
                    longitude: -180 //Lowest
                },
                southeast: {
                    latitude: -90, //Lowest
                    longitude: -180 //Highest
                },
                southwest: {
                    latitude: 90, //Lowest
                    longitude: 180 //Lowest
                }
            };

            var i = 0;
            array.forEach(function (element) {
                //North East Checks
                //Highest
                if (bounds.northeast.latitude < element.latitude) {
                    bounds.northeast.latitude = element.latitude;
                }
                //Highest
                if (bounds.northeast.longitude < element.longitude) {
                    bounds.northeast.longitude = element.longitude;
                }

                //South West Checks
                //Lowest
                if (bounds.southwest.latitude > element.latitude) {
                    bounds.southwest.latitude = element.latitude;
                }
                //Lowest
                if (bounds.southwest.longitude > element.longitude) {
                    bounds.southwest.longitude = element.longitude;
                }

                //North West Checks
                //Highest
                if (bounds.northwest.latitude < element.latitude) {
                    bounds.northwest.latitude = element.latitude;
                }
                //Lowest
                if (bounds.northwest.longitude > element.longitude) {
                    bounds.northwest.longitude = element.longitude;
                }

                //South East Checks
                //Lowest
                if (bounds.southeast.latitude > element.latitude) {
                    bounds.southeast.latitude = element.latitude;
                }
                //Highest
                if (bounds.southeast.longitude < element.longitude) {
                    bounds.southeast.longitude = element.longitude;
                }
                i++;
            });
            return bounds;
        },
        getHiLo: function (array) {
            var hilo = {
                'highest': {
                    'latitude': -90,
                    'longitude': -180,
                },
                'lowest': {
                    'latitude': 90,
                    'longitude': 180,
                }
            };
            array.forEach(function (element) {
                if (element.latitude < hilo.lowest.latitude) {
                    hilo.lowest.latitude = element.latitude;
                }
                if (element.latitude > hilo.highest.latitude) {
                    hilo.highest.latitude = element.latitude;
                }
                if (element.longitude < hilo.lowest.longitude) {
                    hilo.lowest.longitude = element.longitude;
                }
                if (element.longitude > hilo.highest.longitude) {
                    hilo.highest.longitude = element.longitude;
                }
            });
            return hilo;
        },
        getZoomLevelFromBounds: function (locations, mapWidth, mapHeight) {
            var WORLD_DIM = {height: 256, width: 256};
            var ZOOM_MAX = 21;

            function latRad(lat) {
                var sin = Math.sin(lat * Math.PI / 180);
                var radX2 = Math.log((1 + sin) / (1 - sin)) / 2;
                return Math.max(Math.min(radX2, Math.PI), -Math.PI) / 2;
            }

            function zoom(mapPx, worldPx, fraction) {
                return Math.floor(Math.log(mapPx / worldPx / fraction) / Math.LN2);
            }

            var ne = this.getBounds(locations).northeast;
            var sw = this.getBounds(locations).southwest;
            var latFraction = (latRad(ne.latitude) - latRad(sw.latitude)) / Math.PI;

            var lngDiff = ne.longitude - sw.longitude;
            var lngFraction = ((lngDiff < 0) ? (lngDiff + 360) : lngDiff) / 360;

            var latZoom = zoom(mapHeight, WORLD_DIM.height, latFraction);
            var lngZoom = zoom(mapWidth, WORLD_DIM.width, lngFraction);

            return Math.min(latZoom, lngZoom, ZOOM_MAX);
        },
        getAverageCentre: function (locations) {
            var average = {
                latitude: null,
                longitude: null
            };
            locations.forEach(function (element) {
                average.latitude += element.latitude;
                average.longitude += element.longitude;
            });
            average.latitude = average.latitude / locations.length;
            average.longitude = average.longitude / locations.length;
            return average;
        },
        makeMarkerStrings: function makeMarkerStrings(markers) {
            return markers.map(function (marker) {
                var str = Object.keys(marker).map(function (key) {
                    if (STYLE_ATTRIBUTES.indexOf(key) > -1) {
                        return key + ':' + marker[key] + '|';
                    }
                }).join('');

                return str + marker.coords.join(',');
            });
        },
        staticMapFromLocations: function (locations, options) {
            options = options || {};
            if (!options.centre) options.centre = this.getAverageCentre(locations);
            if (!options.zoom) options.zoom = this.getZoomLevelFromBounds(locations, 500, 250);
            if (!options.key) options.key = this.getMapsKey();
            if (!options.maptype) options.maptype = "terrain";
            if (!options.size) options.size = "450x400";
            if (!options.sensor) options.sensor = "false";
            if (!options.format) options.format = "png";
            if (!options.path) {
                options.path = "color:0x3f51b5ff|weight:1|fillcolor:0x00BFA510";
                locations.forEach(function (element1, index) {
                    options.path += '|' + element1.latitude + ',' + element1.longitude;
                    if (index == locations.length - 1) {
                        options.path += '|' + locations[0].latitude + ',' + locations[0].longitude;
                    }
                });
            }
            return this.staticMapify(options);
        },
        staticMapify: function buildSourceString(attrs, markers) {
            var markerStrings;

            if (markers) {
                if (!angular.isArray(markers)) {
                    markers = [markers];
                }
                markerStrings = this.makeMarkerStrings(markers);
            }

            var params = Object.keys(attrs).map(function (attr) {
                if (attr === 'markers' && markerStrings) {
                    return Object.keys(markerStrings).map(function (key) {
                        return 'markers=' + encodeURIComponent(markerStrings[key]);
                    }).join('&');
                }

                if (attr[0] !== '$' && attr !== 'alt') {
                    return encodeURIComponent(attr) + '=' + encodeURIComponent(attrs[attr]);
                }
            });

            return BASE_URL + params.reduce(function (a, b) {
                    if (!a) {
                        return b;
                    }

                    if (b !== undefined) {
                        return a + '&' + b;
                    }

                    return a;
                }, '');
        }
    };

});
