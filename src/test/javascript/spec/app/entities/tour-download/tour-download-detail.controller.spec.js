'use strict';

describe('Controller Tests', function() {

    describe('TourDownload Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockTourDownload, MockUser, MockTour;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockTourDownload = jasmine.createSpy('MockTourDownload');
            MockUser = jasmine.createSpy('MockUser');
            MockTour = jasmine.createSpy('MockTour');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'TourDownload': MockTourDownload,
                'User': MockUser,
                'Tour': MockTour
            };
            createController = function() {
                $injector.get('$controller')("TourDownloadDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tourologistApp:tourDownloadUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
