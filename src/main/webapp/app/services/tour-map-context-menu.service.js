(function () {
    'use strict';

    angular
        .module('tourologistApp.services')
        .service('TourMapContextMenuService', TourMapContextMenuService);

    TourMapContextMenuService.$inject = [];

    function TourMapContextMenuService() {
        var service = this;

        service.init = function (mapControl) {
            service.mapControl = mapControl;
        }

        service.show = function (currentLatLng) {
            setMenuXY(currentLatLng);

            $(".contextmenu").get(0).style.visibility = "visible";
        }

        service.close = function () {
            $(".contextmenu").get(0).style.visibility = "hidden";
        }

        function getCanvasXY(currentLatLng) {
            var scale = Math.pow(2, service.mapControl.getZoom());
            var nw = new google.maps.LatLng(
                service.mapControl.getBounds().getNorthEast().lat(),
                service.mapControl.getBounds().getSouthWest().lng()
            );
            var worldCoordinateNW = service.mapControl.getProjection().fromLatLngToPoint(nw);
            var worldCoordinate = service.mapControl.getProjection().fromLatLngToPoint(currentLatLng);
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
    }
})();
