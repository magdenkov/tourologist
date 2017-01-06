(function () {
    'use strict';

    angular
        .module('tourologistApp.diy-tour')
        .controller('DiyTourController', DiyTourController);

    DiyTourController.$inject = ['$timeout', 'uiGmapIsReady', 'DIYTour', 'TourMapContextMenuService'];

    function DiyTourController($timeout, uiGmapIsReady, DIYTour, mapContextMenu) {
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

        vm.redrawRoutes = function () {
            drawDIYTour();
        }

        vm.initContextMenu = function () {
            google.maps.event.addListener(vm.mapControl, "rightclick", function (event) {
                mapContextMenu.show(event);
            });
        }

        vm.onCircleRightClick = function(event) {
            mapContextMenu.show(event);
        }

        vm.onMenuSetStartPointClick = function () {
            if (vm.startMarker) {
                vm.startMarker.setMap(null);
            }
            vm.startMarker = new google.maps.Marker({
                position: mapContextMenu.currentClickPosition(),
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
                position: mapContextMenu.currentClickPosition(),
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
                    debugger;
                    $timeout(function() {
                        vm.tours = tours;
                    })
                })
            }

        }
    }

})();
