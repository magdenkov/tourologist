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

                scope.showBubblesInRadius.circle ._clickListenerHandler = google.maps.event.addListener(scope.showBubblesInRadius.circle , 'click', function(event) {
                    if (scope.onCircleClick()) {
                        scope.onCircleClick()(event);
                    }
                });

                scope.showBubblesInRadius.circle ._rightClickListenerHandler = google.maps.event.addListener(scope.showBubblesInRadius.circle , 'rightclick', function(event) {
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

                        bubbleMarker._clickListenerHandler = google.maps.event.addListener(bubbleMarker, 'click', function(event) {
                            if (scope.onBubblMarkerClick()) {
                                scope.onBubblMarkerClick()(bubbl, event);
                            }
                        });

                        bubbleMarker._rightClickListenerHandler = google.maps.event.addListener(bubbleMarker, 'rightclick', function(event) {
                            if (scope.onBubblMarkerRightClick()) {
                                scope.onBubblMarkerRightClick()(bubbl, event);
                            }
                        });

                        scope.showBubblesInRadius.bubblMarkers.push(bubbleMarker);
                    })
                })
            }
        }

        uiGmapIsReady.promise().then(function (maps) {
            scope.mapControl = maps[0].map;

            google.maps.event.trigger(scope.mapControl, 'resize');

            scope.mapControl.addListener('center_changed', function () {
                updateBubblesInRadius(this.getCenter());
            });

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

