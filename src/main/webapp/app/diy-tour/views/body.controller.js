(function () {
    'use strict';

    angular
        .module('tourologistApp.diy-tour')
        .controller('DiyTourController', DiyTourController);

    DiyTourController.$inject = ['uiGmapIsReady', 'DIYTour', 'TourMapContextMenuService'];

    function DiyTourController(uiGmapIsReady, DIYTour, mapContextMenu) {
        var vm = this;

        vm.mapControl = null;

        vm.showBubblesInRadius = {
            show: true,
            value: 10000
        };

        vm.mapContextMenuUrl = 'app/diy-tour/views/map-context-menu.html';

        vm.currentClickPosition = null;
        vm.startMarker = null;
        vm.endMarker = null;
        vm.routes = [];
        vm.maxDelta = null;

        uiGmapIsReady.promise().then(function (maps) {
            vm.mapControl = maps[0].map;
            mapContextMenu.init(vm.mapControl);
            vm.initContextMenu();
        })

        vm.redrawRoutes = function () {
            drawDIYTour();
        }

        vm.initContextMenu = function () {
            google.maps.event.addListener(vm.mapControl, "rightclick", function (event) {
                vm.currentClickPosition = event.latLng;
                mapContextMenu.show(event.latLng);
            });
        }

        vm.onCircleRightClick = function(event) {
            vm.currentClickPosition = event.latLng;
            mapContextMenu.show(event.latLng);
        }

        vm.onMenuSetStartPointClick = function () {
            if (vm.startMarker) {
                vm.startMarker.setMap(null);
            }
            vm.startMarker = new google.maps.Marker({
                position: vm.currentClickPosition,
                title: 'start',
                animation: google.maps.Animation.DROP,
                map: vm.mapControl,
                icon: {
                    path: google.maps.SymbolPath.CIRCLE,
                    scale: 10,
                    strokeColor: "darkgray"
                }
            });
            mapContextMenu.close();
            vm.redrawRoutes();
        }

        vm.onMenuSetEndPointClick = function () {
            if (vm.endMarker) {
                vm.endMarker.setMap(null);
            }
            vm.endMarker = new google.maps.Marker({
                position: vm.currentClickPosition,
                title: 'end',
                animation: google.maps.Animation.DROP,
                map: vm.mapControl,
                icon: {
                    path: google.maps.SymbolPath.CIRCLE,
                    scale: 10,
                    strokeColor: "gray"
                }
            });
            mapContextMenu.close();
            vm.redrawRoutes();
        }

        var drawDIYTour = function () {
            var startPosition = vm.startMarker ? vm.startMarker.getPosition() : null;
            var endPosition = vm.endMarker ? vm.endMarker.getPosition() : null;

            var clearRoutes = function () {
                // clear routes
                vm.routes.forEach(function (route) {
                    route.way.setMap(null);
                    route.wayPoints.forEach(function (wayPoint) {
                        wayPoint.setMap(null);
                    })
                    route.wayPoints = [];
                    route.bubbls.forEach(function (bubbl) {
                        bubbl.setMap(null);
                    })
                    route.bubbls = [];
                })

                vm.routes = [];
            }

            clearRoutes();

            if (startPosition != null && endPosition != null) {
                var params = {
                    currentLat: startPosition.lat(),
                    currentLng: startPosition.lng(),
                    targetLat: endPosition.lat(),
                    targetLng: endPosition.lng()
                }
                if (vm.maxDelta) {
                    params.maxDelta = +vm.maxDelta;
                }
                DIYTour.get(params).$promise.then(function (tours) {
                    tours.forEach(function (tour) {
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
                                map: vm.mapControl,
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
                            map: vm.mapControl,
                            title: 'xxxx'
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
                                    path: google.maps.SymbolPath.BACKWARD_CLOSED_ARROW,
                                    scale: 3,
                                    strokeWeight: 3,
                                    strokeColor: "blue"
                                },
                                map: vm.mapControl,
                                strokeColor: "#1637F5"
                            });
                            bubbls.push(bubbleMarker);
                        })

                        var route = {
                            way: way,
                            wayPoints: wayPoints,
                            bubbls: bubbls
                        }
                        vm.routes.push(route);
                    })
                })
            }

        }
    }

})();
