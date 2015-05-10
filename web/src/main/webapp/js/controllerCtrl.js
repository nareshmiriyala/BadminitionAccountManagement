angular.module('registerApp',[])
    .controller('RegController',function($scope,$http){
      $scope.player={};
        $scope.submit=function(){
          console.log("called method");
            $http({
                method: 'POST',
                url: 'http://localhost:8080/web/register',
                headers: {'Content-Type': 'application/json'},
                data:  $scope.player
            });
      };
});
angular.module('hireCourtApp',[])
    .controller('courtHireController',function($scope,$http){
        $scope.courtadded={};

        $scope.courts=[
            { 'CourtName':'Endeavour Hills',
                'DateHired': '10/05/2015',
                'StartTime': '08:30',
                 'EndTime':'10:30',
                 'MoneyPaid':'35.50',
                  'Player':'Naresh'},
            { 'CourtName':'Endeavour Hills',
                'DateHired': '10/05/2015',
                'StartTime': '08:30',
                'EndTime':'10:30',
                'MoneyPaid':'35.50',
                'Player':'Naresh'},
            { 'CourtName':'Endeavour Hills',
                'DateHired': '10/05/2015',
                'StartTime': '08:30',
                'EndTime':'10:30',
                'MoneyPaid':'35.50',
                'Player':'Naresh'},
            { 'CourtName':'Endeavour Hills',
                'DateHired': '10/05/2015',
                'StartTime': '08:30',
                'EndTime':'10:30',
                'MoneyPaid':'35.50',
                'Player':'Vishwash'},
            { 'CourtName':'Endeavour Hills',
                'DateHired': '10/05/2015',
                'StartTime': '08:30',
                'EndTime':'10:30',
                'MoneyPaid':'35.50',
                'Player':'Kiriti'},
        ];
        $scope.addCourt=function(){
            console.log("called method");
            $http({
                method: 'POST',
                url: 'http://localhost:8080/web/hirecourt',
                headers: {'Content-Type': 'application/json'},
                data:  $scope.courtadded
            });
            $scope.courts.push({ 'CourtName':$scope.CourtName, 'DateHired': $scope.DateHired, 'StartTime':$scope.StartTime,
                'EndTime':$scope.EndTime,
                'MoneyPaid':$scope.MoneyPaid,
                'Player':$scope.Player});
            $scope.CourtName='';
            $scope.DateHired='';
            $scope.StartTime='';
            $scope.EndTime='';
            $scope.MoneyPaid='';
            $scope.Player='';
        };
    });
