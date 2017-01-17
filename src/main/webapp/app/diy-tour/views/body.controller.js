(function () {
    'use strict';

    angular
        .module('tourologistApp.diy-tour')
        .controller('DiyTourController', DiyTourController);

    DiyTourController.$inject = ['$state', '$timeout', '$stateParams', 'uiGmapIsReady', 'DIYTour', 'TourMapContextMenuService'];

    function DiyTourController($state, $timeout, $stateParams, uiGmapIsReady, DIYTour, mapContextMenu) {
        var vm = this;

        vm.mapControl = null;

        vm.showBubblesInRadius = {
            show: true,
            value: 10000
        };

        vm.mapContextMenuUrl = 'app/diy-tour/views/map-context-menu.html';

        vm.startMarker = null;
        vm.endMarker = null;
        vm.maxDelta = null;

        uiGmapIsReady.promise().then(function (maps) {
            vm.mapControl = maps[0].map;
            mapContextMenu.init(vm.mapControl);
            vm.initContextMenu();
        })

        vm.initContextMenu = function () {
            google.maps.event.addListener(vm.mapControl, "rightclick", function (event) {
                mapContextMenu.show(event);
            });
        }

        var updateStateParams = function( ){
            $state.go('.', {startPosition: $stateParams.startPosition, endPosition: $stateParams.endPosition}, {notify: false});
        }

        vm.setStartPoint = function (position) {
            if (vm.startMarker) {
                vm.startMarker.setMap(null);
            }
            vm.startMarker = new google.maps.Marker({
                position: position,
                title: 'start',
                animation: google.maps.Animation.DROP,
                map: vm.mapControl,
                icon: {
                    path: google.maps.SymbolPath.CIRCLE,
                    scale: 10,
                    strokeColor: "darkblue"
                }
            });

            $stateParams.startPosition = position.lat() + ',' + position.lng();
            updateStateParams();
        }

        vm.setEndPoint = function (position) {
            if (vm.endMarker) {
                vm.endMarker.setMap(null);
            }
            vm.endMarker = new google.maps.Marker({
                position: position,
                title: 'end',
                animation: google.maps.Animation.DROP,
                map: vm.mapControl,
                icon: {
                    path: google.maps.SymbolPath.CIRCLE,
                    scale: 10,
                    strokeColor: "blue"
                }
            });

            $stateParams.endPosition = position.lat() + ',' + position.lng();
            updateStateParams();
        }

        vm.redrawRoutes = function () {
            var startPosition = vm.startMarker ? vm.startMarker.getPosition() : null;
            var endPosition = vm.endMarker ? vm.endMarker.getPosition() : null;

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
                    $timeout(function () {
                        vm.tours = tours;
                    })
                })
            }

        }

        function getLatLngFromString(ll) {
            var latlng = ll.split(/, ?/);
            return new google.maps.LatLng(parseFloat(latlng[0]), parseFloat(latlng[1]));
        }

        if ($stateParams.startPosition != null && $stateParams.endPosition != null) {
            vm.setStartPoint(getLatLngFromString($stateParams.startPosition));
            vm.setEndPoint(getLatLngFromString($stateParams.endPosition));
            vm.redrawRoutes();
        }

        vm.onCircleRightClick = function (event) {
            mapContextMenu.show(event);
        }

        vm.onMenuSetStartPointClick = function () {
            vm.setStartPoint(mapContextMenu.currentClickPosition());
            mapContextMenu.close();
            vm.redrawRoutes();
        }

        vm.onMenuSetEndPointClick = function () {
            vm.setEndPoint(mapContextMenu.currentClickPosition());
            mapContextMenu.close();
            vm.redrawRoutes();
        }
    }

})();
