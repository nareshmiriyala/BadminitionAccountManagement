angular.module('registerApp', [])
    .controller('RegController', function ($scope, $http) {
        $scope.player = {};
        $scope.registerPlayer = function () {
            console.log("called method register");
            $http({
                method: 'POST',
                url: 'http://localhost:8080/web/register',
                headers: {'Content-Type': 'application/json'},
                data: $scope.player
            });
        };
    });
angular.module('hireCourtApp', [])
    .controller('courtHireController', function ($scope, $http) {
        $scope.addedcourt = {};
        $scope.courts = [];
        $http.get('http://localhost:8080/web/app/hirecourt').success(function (data, status, headers, config) {
            // this callback will be called asynchronously
            // when the response is available
            console.log("Success Get Response");
            $scope.courts = data;
        }).
            error(function (data, status, headers, config) {
                console.log("Error during Get call");
            });
        $scope.addCourt = function () {
            console.log("called method post with data");
            $http.post('http://localhost:8080/web/app/hirecourt', $scope.addedcourt).
                success(function (data, status, headers, config) {
                    // this callback will be called asynchronously
                    // when the response is available
                    console.log("Posted Successfully");
                }).
                error(function (data, status, headers, config) {
                    // called asynchronously if an error occurs
                    // or server returns response with an error status.
                    console.log("error during post call");
                });
            $scope.courts.push({
                'courtName': $scope.addedcourt.courtName, 'dateHired': $scope.addedcourt.dateHired, 'startTime': $scope.addedcourt.startTime,
                'endTime': $scope.addedcourt.endTime,
                'moneyPaid': $scope.addedcourt.moneyPaid,
                'payer': $scope.addedcourt.payer
            });
            $scope.courtName = '';
            $scope.dateHired = '';
            $scope.startTime = '';
            $scope.endTime = '';
            $scope.moneyPaid = '';
            $scope.payer = '';
        };
    });
