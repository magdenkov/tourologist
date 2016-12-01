(function () {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('CreateBubblController', BubblDialogController);

    BubblDialogController.$inject = ['$timeout', '$scope', '$state', 'entity', 'Bubbl', 'Interest',
        'BubblRating', 'BubblDownload', 'Payload', 'BubblAdminReview', 'TourBubbl', 'uiGmapGoogleMapApi', 'SharedProperties', 'Principal'];

    function BubblDialogController($timeout, $scope, $state, entity, Bubbl, Interest, BubblRating,
                                   BubblDownload, Payload, BubblAdminReview, TourBubbl, uiGmapGoogleMapApi, SharedProperties, Principal) {
        var vm = this;

        vm.bubbl = entity;
        vm.save = save;
        vm.interests = Interest.query();
        vm.bubblratings = BubblRating.query();
        vm.bubbldownloads = BubblDownload.query();
        vm.payloads = Payload.query();
        vm.bubbladminreviews = BubblAdminReview.query();
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


        $scope.map = {
            center: {latitude: 54.00366, longitude: -2.547855},

            events: {
                tilesloaded: function (map, eventname, args) {
                    $scope.mapInstance = map;
                    $scope.drawBoundaries();
                }
            }

        };
        uiGmapGoogleMapApi.then(function (maps) {

        });
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
                map: $scope.mapInstance,
                title: $scope.details.name
            });
            $scope.mapInstance.setCenter($scope.details.geometry.location);
            $scope.mapInstance.setZoom(17);  // Why 17? Because it looks good.
            vm.bubbl.name = $scope.details.name;
            $scope.drawingManager.setDrawingMode(google.maps.drawing.OverlayType.CIRCLE);

        });

        var firstLoad = true;
        $scope.drawBoundaries = function () {

            vm.bubbl.userId = $scope.currentUser;

            function updatePoly() {
                vm.bubbl.lat = '';
                vm.bubbl.lng = '';
                vm.bubbl.radiusMeters = '';

                $scope.circle.getPath = function (element, index) {
                    vm.bubbl.lat = $scope.circle.getCenter().lat();
                    vm.bubbl.lng = $scope.circle.getCenter().lng();
                    vm.bubbl.radiusMeters = $scope.circle.getRadius();
                };
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
                    map: $scope.mapInstance
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
                    google.maps.event.addListener($scope.circle.getPath(), "set_at", function (index) {
                        updatePoly();
                    });
                    google.maps.event.addListener($scope.circle.getPath(), "insert_at", function (index) {
                        updatePoly();
                    });
                });
                firstLoad = false;
            }
        };

        $scope.options = {
            watchEnter: true
        };

        $scope.search = function () {
            if ($scope.search.location == '') {
                $scope.errors.search = true;
            }
        };

        $scope.getLocation = function () {
            if (!navigator.geolocation) {
                $scope.error = "Geolocation is not supported by this browser.";
                return;
            }
            navigator.geolocation.getCurrentPosition(function (pos) {
                if ($scope.mapInstance != null) {
                    $scope.mapInstance.setCenter(new google.maps.LatLng(pos.coords.latitude, pos.coords.longitude));
                    $scope.mapInstance.setZoom(17);
                } else {
                    $scope.map.center = {latitude: pos.coords.latitude, longitude: pos.coords.longitude};
                    $scope.map.zoom = 17;
                }
            });

        };
        $scope.getLocation();
        function save() {
            vm.isSaving = true;
            if (vm.bubbl.id !== null) {
                Bubbl.update(vm.bubbl, onSaveSuccess, onSaveError);
            } else {
                Bubbl.save(vm.bubbl, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess(result) {
            $scope.$emit('tourologistApp:bubblUpdate', result);
            SharedProperties.setValue('BubblEntity', vm.bubbl.id);

            $state.go('payload.new');
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }

    }
})();
