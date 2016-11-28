/**
 * Created by rafael on 28/11/2016.
 */
'use strict';

angular
    .module('tourologistApp').service('SharedProperties', function SharedProperties() {
    var hashtable = {};

    return {
        setValue: function (key, value) {
            hashtable[key] = value;
        },
        getValue: function (key) {
            return hashtable[key];
        }
    }
});
