'use strict';

describe('Controller Tests', function() {

    describe('BubblAdminReview Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockBubblAdminReview, MockBubbl, MockUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockBubblAdminReview = jasmine.createSpy('MockBubblAdminReview');
            MockBubbl = jasmine.createSpy('MockBubbl');
            MockUser = jasmine.createSpy('MockUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'BubblAdminReview': MockBubblAdminReview,
                'Bubbl': MockBubbl,
                'User': MockUser
            };
            createController = function() {
                $injector.get('$controller')("BubblAdminReviewDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tourologistApp:bubblAdminReviewUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
