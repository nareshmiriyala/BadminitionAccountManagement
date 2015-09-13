/**
 * Main AngularJS Web Application
 */
var app = angular.module('badmintonWebApp', [
    'ngRoute'
]);

/**
 * Configure the Routes
 */
app.config(['$routeProvider', function ($routeProvider) {
        $routeProvider
                // Home
                .when("/",
                        {
                            templateUrl: "pages/home.html",
                            controller: "PageCtrl"
                        })
                // Pages
                .when("/about",
                        {
                            templateUrl: "pages/about.html",
                            controller: "PageCtrl"
                        })
                .when("/faq",
                        {
                            templateUrl: "pages/faq.html",
                            controller: "PageCtrl"
                        })
                .when("/pricing",
                        {
                            templateUrl: "pages/pricing.html",
                            controller: "PageCtrl"
                        })
                .when("/services",
                        {
                            templateUrl: "pages/services.html",
                            controller: "PageCtrl"
                        })
                .when("/contact",
                        {
                            templateUrl: "pages/contact.html",
                            controller: "PageCtrl"
                        })
                // Blog
                .when("/blog",
                        {
                            templateUrl: "pages/blog.html",
                            controller: "BlogCtrl"
                        })
                .when("/blog/post",
                        {
                            templateUrl: "pages/blog_item.html",
                            controller: "BlogCtrl"
                        })
                .when("/login",
                        {
                            templateUrl: "pages/login.html",
                            controller: "PageCtrl"
                        })
                // else 404
                .otherwise("/404",
                        {
                            templateUrl: "pages/404.html",
                            controller: "PageCtrl"
                        });
    }]);

