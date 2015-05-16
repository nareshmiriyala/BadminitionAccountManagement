angular.module('registerApp',[])
    .controller('RegController',function($scope,$http){
      $scope.payer={};
        $scope.submit=function(){
          console.log("called method");
            $http({
                method: 'POST',
                url: 'http://localhost:8080/web/register',
                headers: {'Content-Type': 'application/json'},
                data:  $scope.payer
            });
      };
});
angular.module('hireCourtApp',[])
    .controller('courtHireController',function($scope,$http){
        $scope.courtadded={};

        //$scope.courts=[
        //    { 'courtName':'Endeavour Hills',
        //        'dateHired': '10/05/2015',
        //        'startTime': '08:30',
        //         'endTime':'10:30',
        //         'moneyPaid':'35.50',
        //          'payer':'Naresh'},
        //    { 'courtName':'Endeavour Hills',
        //        'dateHired': '10/05/2015',
        //        'startTime': '08:30',
        //        'endTime':'10:30',
        //        'moneyPaid':'35.50',
        //        'payer':'Naresh'},
        //    { 'courtName':'Endeavour Hills',
        //        'dateHired': '10/05/2015',
        //        'startTime': '08:30',
        //        'endTime':'10:30',
        //        'moneyPaid':'35.50',
        //        'payer':'Naresh'},
        //    { 'courtName':'Endeavour Hills',
        //        'dateHired': '10/05/2015',
        //        'startTime': '08:30',
        //        'endTime':'10:30',
        //        'moneyPaid':'35.50',
        //        'payer':'Vishwash'},
        //    { 'courtName':'Endeavour Hills',
        //        'dateHired': '10/05/2015',
        //        'startTime': '08:30',
        //        'endTime':'10:30',
        //        'moneyPaid':'35.50',
        //        'payer':'Kiriti'},
        //];
        $scope.courts=[];
        $http.get('http://localhost:8080/web/app/hirecourt'). success(function(data, status, headers, config) {
            // this callback will be called asynchronously
            // when the response is available
            console.log("Success Get Response");
            $scope.courts=data;
        }).
            error(function(data, status, headers, config) {
                // called asynchronously if an error occurs
                // or server returns response with an error status.
                console.log("Error during Get call");
            });
        $scope.addCourt=function(){
            console.log("called method");
            $http({
                method: 'POST',
                url: 'http://localhost:8080/web/app/hirecourt',
                headers: {'Content-Type': 'application/json'},
                data:  $scope.courtadded
            });
            $scope.courts.push({ 'courtName':$scope.courtName, 'dateHired': $scope.dateHired, 'startTime':$scope.startTime,
                'endTime':$scope.endTime,
                'moneyPaid':$scope.moneyPaid,
                'payer':$scope.payer});
            $scope.courtName='';
            $scope.dateHired='';
            $scope.startTime='';
            $scope.endTime='';
            $scope.moneyPaid='';
            $scope.payer='';
        };
    });
