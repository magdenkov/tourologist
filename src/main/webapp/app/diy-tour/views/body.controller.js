(function () {
    'use strict';

    angular
        .module('tourologistApp.diy-tour')
        .controller('DiyTourController', DiyTourController);

    DiyTourController.$inject = ['uiGmapIsReady', 'DIYTour'];

    function DiyTourController(uiGmapIsReady, DIYTour) {
        var vm = this;

        vm.mapControl = null;

        vm.currentClickPosition = null;
        vm.startMarker = null;
        vm.endMarker = null;
        vm.routes = [];
        vm.maxDelta = null;

        uiGmapIsReady.promise().then(function (maps) {
            vm.mapControl = maps[0].map;

            vm.initContextMenu();
        })

        var closeContextMenu = function () {
            $(".contextmenu").get(0).style.visibility = "hidden";
        }

        vm.redrawRoutes = function () {
            drawDIYTour();
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
            closeContextMenu();
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
            closeContextMenu();
            vm.redrawRoutes();
        }

        vm.initContextMenu = function () {
            google.maps.event.addListener(vm.mapControl, "rightclick", function (event) {
                vm.currentClickPosition = event.latLng;
                showContextMenu(event.latLng);
            });

            function getCanvasXY(currentLatLng) {
                var scale = Math.pow(2, vm.mapControl.getZoom());
                var nw = new google.maps.LatLng(
                    vm.mapControl.getBounds().getNorthEast().lat(),
                    vm.mapControl.getBounds().getSouthWest().lng()
                );
                var worldCoordinateNW = vm.mapControl.getProjection().fromLatLngToPoint(nw);
                var worldCoordinate = vm.mapControl.getProjection().fromLatLngToPoint(currentLatLng);
                var currentLatLngOffset = new google.maps.Point(
                    Math.floor((worldCoordinate.x - worldCoordinateNW.x) * scale),
                    Math.floor((worldCoordinate.y - worldCoordinateNW.y) * scale)
                );
                return currentLatLngOffset;
            }

            function setMenuXY(currentLatLng) {
                var mapWidth = $('#map-canvas').width();
                var mapHeight = $('#map-canvas').height();
                var menuWidth = $('.contextmenu').width();
                var menuHeight = $('.contextmenu').height();
                var clickedPosition = getCanvasXY(currentLatLng);
                var x = clickedPosition.x;
                var y = clickedPosition.y;

                if ((mapWidth - x ) < menuWidth)
                    x = x - menuWidth;
                if ((mapHeight - y ) < menuHeight)
                    y = y - menuHeight;

                $('.contextmenu').css('left', x);
                $('.contextmenu').css('top', y);
            };

            function showContextMenu(currentLatLng) {
                setMenuXY(currentLatLng);

                $(".contextmenu").get(0).style.visibility = "visible";
            }
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
                                labelContent: "Bubble: " + bubble.name,
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
