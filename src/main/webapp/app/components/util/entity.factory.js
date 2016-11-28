/**
 * Created by rafael on 28/11/2016.
 */
(function () {
    'use strict';


    angular.module('tourologistApp').factory('Entity', ["Mutator", function (Mutator) {
        function Entity() {

        }

        Entity.prototype.attributes = function (arr) {
            var obj = {};
            if (Array.isArray(arr)) {
                arr.forEach(function (element) {
                    obj[element] = this[element];
                }.bind(this))
            } else {
                obj[arr] = this[arr]
            }
            console.log(obj);
            return obj;
        };

        Entity.build = function (data) {
            // var entity = new this();
            var entity = angular.extend(new this(), Entity.prototype);
            for (var variable in data) {
                if (!data.hasOwnProperty(variable)) continue;
                if (entity[variable] instanceof Mutator) {
                    entity[variable] = entity[variable].transform(data[variable]);
                    continue;
                }
                entity[variable] = data[variable];
            }
            for (var variable in entity) {
                if (entity[variable] instanceof Mutator) {
                    entity[variable] = entity[variable].transform(null);
                }
            }
            if (entity.clone)
                entity.clone(entity);
            return entity;
        };

        return Entity;
    }]);

    Array.prototype.getEntity = function (id) {
        var entity = null;
        this.some(function (element) {
            if (element.id === id) {
                entity = element;
                return true;
            }
            return false;
        });
        return entity;
    };

    Array.prototype.getEntityIndex = function (id) {
        var entity = null;
        this.some(function (element, index) {
            if (element.id === id) {
                entity = index;
                return true;
            }
            return false;
        });
        return entity;
    };

    Array.prototype.removeEntity = function (id) {
        var index = this.getEntityIndex(id);
        if (typeof index === "undefined") {
            console.groupCollapsed('Couldn\'t find the correct index for entity: ' + id + ' in array');
            console.log(this);
            console.groupEnd();
            return;
        }
        this.splice(index, 1);
    }
})();
