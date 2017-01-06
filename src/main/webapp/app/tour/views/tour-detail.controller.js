(function () {
    'use strict';

    angular
        .module('tourologistApp.tour')
        .controller('TourDetailController', TourDetailController);

    TourDetailController.$inject = ['$scope', '$rootScope', 'previousState', 'entity', 'uiGmapIsReady'];

    function TourDetailController($scope, $rootScope, previousState, entity, uiGmapIsReady) {
        var vm = this;
        vm.previousState = previousState.name;
        vm.mapControl = null;
        vm.route = null;

        vm.setTour = function(_tour) {
            vm.tour = _tour;
        }

        vm.setTour(entity);

        uiGmapIsReady.promise().then(function (maps) {
            vm.mapControl = maps[0].map;
           // drawTourRoute();
        })

       /* var drawTourRoute = function () {
            var mapControl = vm.mapControl;

            var clearRoute = function () {
                if (vm.route && vm.route.way) {
                    vm.route.way.setMap(null);
                    vm.route.way = null;
                }
                if (vm.route && vm.route.wayPoints) {
                    vm.route.wayPoints.forEach(function (wayPoint) {
                        wayPoint.setMap(null);
                    })
                    vm.route.wayPoint = [];
                }
                if (vm.route && vm.route.bubbls) {
                    vm.route.bubbls.forEach(function (bubbl) {
                        bubbl.setMap(null);
                    })
                    vm.route.bubbls = [];
                }
            }

            clearRoute();

            var coordinates = [];
            var wayPoints = [];

            _.orderBy(vm.tour.tourRoutePoints, 'orderNumber').forEach(function (tourRoutePoint) {
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

            vm.tour.bubbls.forEach(function (bubble) {
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
                    map: mapControl,
                    strokeColor: "#1637F5"
                });
                bubbls.push(bubbleMarker);
            })

            vm.route = {
                way: way,
                wayPoints: wayPoints,
                bubbls: bubblsvm.tour
        } */

        var unsubscribe = $rootScope.$on('tourologistApp:tourUpdate', function (event, result) {
            vm.setTour(result);
          /*  if (vm.mapControl != null) {
                drawTourRoute();
            }*/
        });

        $scope.$on('$destroy', unsubscribe);
    }
})();
