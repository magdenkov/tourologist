'use strict';

describe('Controller Tests', function() {

    describe('Tour Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockTour, MockUser, MockInterest, MockTourRating, MockTourDownload, MockTourImage, MockTourAdminReview, MockTourRoutePoint, MockTourBubbl;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockTour = jasmine.createSpy('MockTour');
            MockUser = jasmine.createSpy('MockUser');
            MockInterest = jasmine.createSpy('MockInterest');
            MockTourRating = jasmine.createSpy('MockTourRating');
            MockTourDownload = jasmine.createSpy('MockTourDownload');
            MockTourImage = jasmine.createSpy('MockTourImage');
            MockTourAdminReview = jasmine.createSpy('MockTourAdminReview');
            MockTourRoutePoint = jasmine.createSpy('MockTourRoutePoint');
            MockTourBubbl = jasmine.createSpy('MockTourBubbl');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Tour': MockTour,
                'User': MockUser,
                'Interest': MockInterest,
                'TourRating': MockTourRating,
                'TourDownload': MockTourDownload,
                'TourImage': MockTourImage,
                'TourAdminReview': MockTourAdminReview,
                'TourRoutePoint': MockTourRoutePoint,
                'TourBubbl': MockTourBubbl
            };
            createController = function() {
                $injector.get('$controller')("TourDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tourologistApp:tourUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
