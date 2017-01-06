(function () {
    'use strict';

    angular
        .module('tourologistApp.directives')
        .directive('tourGoogleMap', tourGoogleMap);

    tourGoogleMap.$inject = [];

    function tourGoogleMap() {
        return {
            restrict: 'AE',
            templateUrl: 'app/directives/tour-google-map/tour-google-map.html',
            scope: {
                showBubbles: '=',
                radius: '=',
                tours: '=',
                onlyCenterMapByTours: '=',
                onCircleClick: '&',
                onCircleRightClick: '&',
                onBubblMarkerClick: '&',
                onBubblMarkerRightClick: '&'
            },
            link: function (scope, element, attrs) {
            },
            controller: ['$scope', 'InitialMapConfig', 'uiGmapIsReady', 'Bubbl', controller]
        }
    }

    function controller(scope, initialMapConfig, uiGmapIsReady, Bubbl) {
        scope.mapConfig = initialMapConfig.call();
        scope.mapControl = null;

        scope.onlyCenterMapByTours = scope.onlyCenterMapByTours || false;

        scope.showBubblesInRadius = {
            show: scope.showBubbles || false,
            radius: +scope.radius || 10000,
            bubblMarkers: [],
            circle: null
        };

        var events = {
            places_changed: function (searchBox) {
                var place = searchBox.getPlaces();
                if (!place || place == 'undefined' || place.length == 0) {
                    console.log('no place data :(');
                    return;
                }

                scope.mapConfig = initialMapConfig.call(place[0].geometry.location.lat(), place[0].geometry.location.lng(), 18);
            }
        };

        scope.searchbox = {template: 'app/directives/tour-google-map/searchbox.tpl.html', events: events};

        var updateBubblesInRadius = function (center) {
            if (scope.showBubblesInRadius.circle) {
                if (scope.showBubblesInRadius.circle._clickListenerHandler != null) {
                    google.maps.event.removeListener(scope.showBubblesInRadius.circle._clickListenerHandler);
                }
                if (scope.showBubblesInRadius.circle._rightClickListenerHandler != null) {
                    google.maps.event.removeListener(scope.showBubblesInRadius.circle._rightClickListenerHandler);
                }
                scope.showBubblesInRadius.circle.setMap(null);
            }

            scope.showBubblesInRadius.circle = null;

            scope.showBubblesInRadius.bubblMarkers.forEach(function (bubblMarker) {
                if (bubblMarker._clickListenerHandler != null) {
                    google.maps.event.removeListener(bubblMarker._clickListenerHandler);
                }
                if (bubblMarker._rightClickListenerHandler != null) {
                    google.maps.event.removeListener(bubblMarker._rightClickListenerHandler);
                }
                bubblMarker.setMap(null);
            })

            scope.showBubblesInRadius.bubblMarkers = [];

            if (scope.showBubblesInRadius.show === true) {
                scope.showBubblesInRadius.circle = new google.maps.Circle({
                    strokeColor: 'red',
                    strokeOpacity: 0.5,
                    strokeWeight: 2,
                    fillColor: 'yellow',
                    fillOpacity: 0.1,
                    map: scope.mapControl,
                    center: center,
                    radius: +scope.showBubblesInRadius.radius
                });

                scope.showBubblesInRadius.circle._clickListenerHandler = google.maps.event.addListener(scope.showBubblesInRadius.circle, 'click', function (event) {
                    if (scope.onCircleClick()) {
                        scope.onCircleClick()(event);
                    }
                });

                scope.showBubblesInRadius.circle._rightClickListenerHandler = google.maps.event.addListener(scope.showBubblesInRadius.circle, 'rightclick', function (event) {
                    if (scope.onCircleRightClick()) {
                        scope.onCircleRightClick()(event);
                    }
                });

                var params = {
                    centerLat: center.lat(),
                    centerLng: center.lng(),
                    radius: +scope.showBubblesInRadius.radius
                }

                Bubbl.in_radius(params).$promise.then(function (bubbles) {
                    bubbles.forEach(function (bubbl) {
                        var bubbleMarker = new MarkerWithLabel({
                            bubbl: bubbl,
                            position: new google.maps.LatLng(bubbl.lat, bubbl.lng),
                            title: bubbl.name,
                            labelContent: bubbl.name,
                            labelClass: "labels",
                            labelStyle: {
                                opacity: 1.0,
                                color: "black",
                                background: 'white',
                                padding: '2px',
                                margin: '2px'
                            },
                            zIndex: 999999,
                            icon: {
                                path: google.maps.SymbolPath.BACKWARD_CLOSED_ARROW,
                                scale: 3,
                                strokeWeight: 2,
                                strokeColor: "red"
                            },
                            map: scope.mapControl,
                            strokeColor: "#1637F5"
                        });

                        bubbleMarker._clickListenerHandler = google.maps.event.addListener(bubbleMarker, 'click', function (event) {
                            if (scope.onBubblMarkerClick()) {
                                scope.onBubblMarkerClick()(bubbl, event);
                            }
                        });

                        bubbleMarker._rightClickListenerHandler = google.maps.event.addListener(bubbleMarker, 'rightclick', function (event) {
                            if (scope.onBubblMarkerRightClick()) {
                                scope.onBubblMarkerRightClick()(bubbl, event);
                            }
                        });

                        scope.showBubblesInRadius.bubblMarkers.push(bubbleMarker);
                    })
                })
            }
        }

        scope.route = null;

        var drawTourRoute = function (tour) {
            var mapControl = scope.mapControl;

            var clearRoute = function () {
                if (scope.route && scope.route.way) {
                    scope.route.way.setMap(null);
                    scope.route.way = null;
                }
                if (scope.route && scope.route.wayPoints) {
                    scope.route.wayPoints.forEach(function (wayPoint) {
                        wayPoint.setMap(null);
                    })
                    scope.route.wayPoint = [];
                }
                if (scope.route && scope.route.bubbls) {
                    scope.route.bubbls.forEach(function (bubbl) {
                        bubbl.setMap(null);
                    })
                    scope.route.bubbls = [];
                }
            }

            clearRoute();

            var coordinates = [];
            var wayPoints = [];

            _.orderBy(tour.tourRoutePoints, 'orderNumber').forEach(function (tourRoutePoint) {
                coordinates.push({lat: tourRoutePoint.lat, lng: tourRoutePoint.lng});

                var wayPoint = new MarkerWithLabel({
                    position: new google.maps.LatLng(tourRoutePoint.lat, tourRoutePoint.lng),
                    labelContent: tourRoutePoint.orderNumber,
                    labelClass: "labels",
                    labelStyle: {
                        opacity: 0.90,
                        color: "black",
                        background: 'transparent',
                        padding: '2px',
                        margin: '2px',
                        'font-size': '8px'
                    },
                    zIndex: 999999,
                    icon: {
                        path: google.maps.SymbolPath.CIRCLE,
                        scale: 1
                    },
                    map: mapControl,
                    strokeColor: "#1637F5"
                });
                wayPoints.push(wayPoint);
            })

            var way = new google.maps.Polyline({
                path: coordinates,
                geodesic: true,
                strokeColor: 'red',
                strokeOpacity: 0.5,
                strokeWeight: 10,
                map: mapControl,
                title: 'way'
            });

            var bubbls = [];

            tour.bubbls.forEach(function (bubble) {
                var bubbleMarker = new MarkerWithLabel({
                    position: new google.maps.LatLng(bubble.lat, bubble.lng),
                    title: bubble.name,
                    labelContent: bubble.name,
                    labelClass: "labels",
                    labelStyle: {
                        opacity: 0.75,
                        color: "yellow",
                        background: 'black',
                        padding: '2px',
                        margin: '2px'
                    },
                    zIndex: 999999,
                    icon: {
                        path: google.maps.SymbolPath.CIRCLE,
                        scale: 3,
                        strokeWeight: 3,
                        strokeColor: "blue"
                    },
                    map: mapControl,
                    strokeColor: "#1637F5"
                });
                bubbls.push(bubbleMarker);
            })

            scope.route = {
                way: way,
                wayPoints: wayPoints,
                bubbls: bubbls
            }
        }

        scope.$watch('tours', function (newValue, oldValue) {
            if (newValue !== oldValue) {
                if (scope.mapControl != null) {
                    if (newValue != null && newValue.length > 0) {
                        newValue.forEach(function (tour) {
                            if (scope.onlyCenterMapByTours === false) {
                                drawTourRoute(tour);
                            }
                        })
                    }
                }
            }
        }, true);

        uiGmapIsReady.promise().then(function (maps) {
            scope.mapControl = maps[0].map;

            scope.mapControl.addListener('center_changed', function () {
                updateBubblesInRadius(this.getCenter());
            });

            if (scope.tours != null && scope.tours.length > 0) {
                var mapBounds = new google.maps.LatLngBounds();

                scope.tours.forEach(function (tour) {
                    if (scope.onlyCenterMapByTours === false) {
                        drawTourRoute(tour);
                    }

                    if (tour.tourRoutePoints != null) {
                        tour.tourRoutePoints.forEach(function (tourRoutePoint) {
                            mapBounds.extend(new google.maps.LatLng(tourRoutePoint.lat, tourRoutePoint.lng));
                        })
                    }
                })

                scope.mapConfig.center = {latitude: mapBounds.getCenter().lat(), longitude: mapBounds.getCenter().lng()};
                scope.mapConfig.zoom = 15;
            }

            google.maps.event.trigger(scope.mapControl, 'resize');

            scope.$watch('showBubbles', function (newValue, oldValue) {
                if (newValue != oldValue) {
                    scope.showBubblesInRadius.show = newValue;
                    updateBubblesInRadius(scope.mapControl.getCenter());
                }
            })

            scope.$watch('radius', function (newValue, oldValue) {
                if (newValue != oldValue) {
                    scope.showBubblesInRadius.radius = newValue;
                    updateBubblesInRadius(scope.mapControl.getCenter());
                }
            })
        })
    }

})();

