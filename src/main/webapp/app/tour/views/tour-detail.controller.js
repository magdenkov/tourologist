(function () {
    'use strict';

    angular
        .module('tourologistApp.tour')
        .controller('TourDetailController', TourDetailController);

    TourDetailController.$inject = ['$scope', '$rootScope', 'previousState', 'entity', 'Tour', 'User', 'Interest', 'TourRating', 'TourDownload', 'TourImage', 'TourAdminReview', 'TourRoutePoint', 'TourBubbl'];

    function TourDetailController($scope, $rootScope, previousState, entity, Tour, User, Interest, TourRating, TourDownload, TourImage, TourAdminReview, TourRoutePoint, TourBubbl) {
        var vm = this;
        var tourRoutePoints = [];
        vm.tour = entity;
        vm.previousState = previousState.name;
        var lat = [];
        var lng = [];
        var myCoordinates = [];

        function initialize() {
            $scope.map = {
                center: {latitude: vm.tour.lat, longitude: vm.tour.lng}, zoom: 15, bounds: {}, events: {
                    tilesloaded: function (map, eventname, args) {
                        $scope.mapInstance = map;
                    }
                }
            };
            console.log(vm.tour.tourRoutePoints.length);
            for (var i = 0; i < vm.tour.tourRoutePoints.length; i++) {
                lat = vm.tour.tourRoutePoints[i].lat;
                lng = vm.tour.tourRoutePoints[i].lng;
                myCoordinates.push(new google.maps.LatLng(lat, lng));

            }
            $scope.back = function () {
                window.history.back()
            }

            $scope.routes = [{

                path: myCoordinates,
                stroke: {
                    color: '#6060FB',
                    weight: 3
                },
                editable: false,
                draggable: false,
                geodesic: true,
                visible: true,
                icons: [{
                    icon: {
                        path: google.maps.SymbolPath.BACKWARD_OPEN_ARROW
                    },
                    offset: '25px',
                    repeat: '50px'
                }]
            }]


        }

        initialize();
        google.maps.event.addDomListener(window, 'load', initialize);

        var unsubscribe = $rootScope.$on('tourologistApp:tourUpdate', function (event, result) {
            vm.tour = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
