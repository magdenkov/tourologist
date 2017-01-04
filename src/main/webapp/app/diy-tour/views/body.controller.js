(function () {
    'use strict';

    angular
        .module('tourologistApp.diy-tour')
        .controller('DiyTourController', DiyTourController);

    DiyTourController.$inject = ['$scope', '$state', 'uiGmapIsReady', 'InitialMapConfigForDiyTour'];

    function DiyTourController($scope, $state, uiGmapIsReady, initialMapConfig) {
        var vm = this;

        vm.mapControl = null;
        vm.mapConfig = initialMapConfig.call();

        vm.currentClickPosition = null;
        vm.startMarker = null;
        vm.endMarker = null;

        uiGmapIsReady.promise().then(function (maps) {
            vm.mapControl = maps[0].map;

            vm.initContextMenu();
        })

        var events = {
            places_changed: function (searchBox) {
                var place = searchBox.getPlaces();
                if (!place || place == 'undefined' || place.length == 0) {
                    console.log('no place data :(');
                    return;
                }

                vm.mapConfig = initialMapConfig.call(place[0].geometry.location.lat(), place[0].geometry.location.lng(), 18);
            }
        };

        vm.searchbox = {template: 'app/diy-tour/views/searchbox.tpl.html', events: events};

        var closeContextMenu = function() {
            $(".contextmenu").get(0).style.visibility = "hidden";
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
                    scale: 10
                }
            });
            closeContextMenu();
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
                    scale: 10
                }
            });
            closeContextMenu();
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

    }
})();
