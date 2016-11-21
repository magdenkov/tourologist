'use strict';

describe('Controller Tests', function() {

    describe('Bubbl Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockBubbl, MockUser, MockInterest, MockBubblRating, MockBubblDownload, MockPayload, MockBubblAdminReview, MockTourBubbl;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockBubbl = jasmine.createSpy('MockBubbl');
            MockUser = jasmine.createSpy('MockUser');
            MockInterest = jasmine.createSpy('MockInterest');
            MockBubblRating = jasmine.createSpy('MockBubblRating');
            MockBubblDownload = jasmine.createSpy('MockBubblDownload');
            MockPayload = jasmine.createSpy('MockPayload');
            MockBubblAdminReview = jasmine.createSpy('MockBubblAdminReview');
            MockTourBubbl = jasmine.createSpy('MockTourBubbl');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Bubbl': MockBubbl,
                'User': MockUser,
                'Interest': MockInterest,
                'BubblRating': MockBubblRating,
                'BubblDownload': MockBubblDownload,
                'Payload': MockPayload,
                'BubblAdminReview': MockBubblAdminReview,
                'TourBubbl': MockTourBubbl
            };
            createController = function() {
                $injector.get('$controller')("BubblDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tourologistApp:bubblUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
