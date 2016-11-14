'use strict';

describe('Controller Tests', function() {

    describe('BubblDownload Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockBubblDownload, MockUser, MockBubbl;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockBubblDownload = jasmine.createSpy('MockBubblDownload');
            MockUser = jasmine.createSpy('MockUser');
            MockBubbl = jasmine.createSpy('MockBubbl');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'BubblDownload': MockBubblDownload,
                'User': MockUser,
                'Bubbl': MockBubbl
            };
            createController = function() {
                $injector.get('$controller')("BubblDownloadDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tourologistApp:bubblDownloadUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
