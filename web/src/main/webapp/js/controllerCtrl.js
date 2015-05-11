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

        $scope.courts=[
            { 'courtName':'Endeavour Hills',
                'dateHired': '10/05/2015',
                'startTime': '08:30',
                 'endTime':'10:30',
                 'moneyPaid':'35.50',
                  'payer':'Naresh'},
            { 'courtName':'Endeavour Hills',
                'dateHired': '10/05/2015',
                'startTime': '08:30',
                'endTime':'10:30',
                'moneyPaid':'35.50',
                'payer':'Naresh'},
            { 'courtName':'Endeavour Hills',
                'dateHired': '10/05/2015',
                'startTime': '08:30',
                'endTime':'10:30',
                'moneyPaid':'35.50',
                'payer':'Naresh'},
            { 'courtName':'Endeavour Hills',
                'dateHired': '10/05/2015',
                'startTime': '08:30',
                'endTime':'10:30',
                'moneyPaid':'35.50',
                'payer':'Vishwash'},
            { 'courtName':'Endeavour Hills',
                'dateHired': '10/05/2015',
                'startTime': '08:30',
                'endTime':'10:30',
                'moneyPaid':'35.50',
                'payer':'Kiriti'},
        ];
        $scope.addCourt=function(){
            console.log("called method");
            $http({
                method: 'POST',
                url: 'http://localhost:8080/web/hirecourt',
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
