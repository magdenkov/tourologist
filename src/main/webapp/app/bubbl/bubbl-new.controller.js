(function () {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('CreateBubblController', BubblDialogController);

    BubblDialogController.$inject = ['$scope', '$state', 'entity', 'Bubbl', 'Interest',
        'Payload', 'TourBubbl', 'uiGmapIsReady', 'SharedProperties', 'InitialMapConfig'];

    function BubblDialogController($scope, $state, entity, Bubbl, Interest, Payload, TourBubbl, uiGmapIsReady, SharedProperties, initialMapConfig) {
        var vm = this;


        vm.mapConfig = initialMapConfig.call();
        vm.mapControl = null;

        vm.bubbl = entity;
        vm.save = save;
        vm.interests = Interest.query();
        vm.payloads = Payload.query();
        vm.tourbubbls = TourBubbl.query();

        vm.bubbl = {
            name: '',
            lat: '',
            lng: '',
            radiusMeters: '',
            id: '',
            status: 'SUBMITTED'
        };

        $scope.user = {
            interest: [vm.interests[0]]
        };

        $scope.lat = "0";
        $scope.lng = "0";

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

        vm.searchbox = {template: 'app/directives/tour-google-map/searchbox.tpl.html', events: events};

        uiGmapIsReady.promise().then(function (maps) {
            vm.mapControl = maps[0].map;

            vm.getLocation();

            vm.drawBoundaries();
            google.maps.event.trigger(vm.mapControl, 'resize');
        })

        $scope.removeLine = function () {
            $scope.circle.setMap(null);
            $scope.showFinishButton = false;
            $scope.drawingManager.setDrawingMode(google.maps.drawing.OverlayType.CIRCLE);
            $scope.drawingManager.setOptions({
                drawingControl: true
            });
        };

        $scope.showFinishButton = false;

        $scope.$watch('details', function () {
            if (!$scope.details) {
                return;
            }
            if ($scope.marker) {
                $scope.marker.setMap(null);
            }
            $scope.marker = new google.maps.Marker({
                position: $scope.details.geometry.location,
                map: vm.mapControl,
                title: $scope.details.name
            });

            vm.mapControl.setCenter($scope.details.geometry.location);
            vm.mapControl.setZoom(17);  // Why 17? Because it looks good.
            vm.bubbl.name = $scope.details.name;
            $scope.drawingManager.setDrawingMode(google.maps.drawing.OverlayType.CIRCLE);

        });

        var firstLoad = true;

        vm.drawBoundaries = function () {
            vm.bubbl.userId = $scope.currentUser;

            function updatePoly() {
                vm.bubbl.lat = $scope.circle.getCenter().lat();
                vm.bubbl.lng = $scope.circle.getCenter().lng();
                vm.bubbl.radiusMeters = $scope.circle.getRadius();

                $scope.circle.getPath = function (element, index) {
                    vm.bubbl.lat = $scope.circle.getCenter().lat();
                    vm.bubbl.lng = $scope.circle.getCenter().lng();
                    vm.bubbl.radiusMeters = $scope.circle.getRadius();
                };
                console.log("lat " + vm.bubbl.lat + " lng " + vm.bubbl.lng)
            }

            if (firstLoad) {
                var shapeOptions = {
                    strokeWeight: 4,
                    strokeOpacity: 1,
                    fillOpacity: 0.2,
                    editable: true,
                    clickable: true,
                    draggable: true,
                    radius: 500000,

                    strokeColor: '#f09609',
                    fillColor: '#08B21F'
                };

                $scope.getButtonPosition = function () {
                    if (window.width < 400) {
                        return google.maps.ControlPosition.RIGHT_BOTTOM;
                    }
                    return google.maps.ControlPosition.TOP_LEFT;
                };

                $scope.drawingManager = new google.maps.drawing.DrawingManager({
                    drawingMode: google.maps.drawing.OverlayType.CIRCLE,
                    drawingControlOptions: {
                        position: $scope.getButtonPosition(),
                        drawingModes: [google.maps.drawing.OverlayType.CIRCLE],
                    },
                    circleOptions: shapeOptions,
                    map: vm.mapControl

                });

                google.maps.event.addDomListener($scope.drawingManager, 'circlecomplete', function (circle) {
                    $scope.$apply(function () {
                        $scope.showFinishButton = true;
                    });
                    $scope.circle = circle;
                    circle.setEditable(true);
                    $scope.drawingManager.setOptions({
                        drawingControl: false
                    });
                    $scope.drawingManager.setDrawingMode(null);
                    updatePoly();
                    google.maps.event.addListener($scope.circle, "set_at", function (index) {
                        updatePoly();
                    });
                    google.maps.event.addListener($scope.circle, "insert_at", function (index) {
                        updatePoly();
                    });
                    google.maps.event.addListener($scope.circle, "dragend", function (index) {
                        updatePoly();
                    });

                });
                firstLoad = false;
            }
        };

        vm.getLocation = function () {
            if (!navigator.geolocation) {
                $scope.error = "Geolocation is not supported by this browser.";
                return;
            }
            navigator.geolocation.getCurrentPosition(function (pos) {
                vm.mapControl.setCenter(new google.maps.LatLng(pos.coords.latitude, pos.coords.longitude));
                vm.mapControl.setZoom(17);
            });
        };

        function save() {
            vm.isSaving = true;
            Bubbl.save(vm.bubbl, onSaveSuccess, onSaveError);
        }

        function onSaveSuccess(result) {
            $scope.$emit('tourologistApp:bubblUpdate', result);
            SharedProperties.setValue('BubblEntity', vm.bubbl.id);
            $state.go('payload.new', {bubblId: result.id});
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }

        $scope.back = function () {
            window.history.back();
        };

    }
})();
