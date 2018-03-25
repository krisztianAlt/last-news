var app = app || {};

app.init = function () {
    app.europeMap.showMap();
};

app.europeMap = {

    showMap: function () {
        // CSSMap;
        $("#map-europe").CSSMap({
            "size": 650,
            "tooltips": "floating-top-center",
            "responsive": "auto",
            "cities": true,
            onClick: function(e){
                var link = e.children("A").eq(0).attr("href"),
                    text = e.children("A").eq(0).text(),
                    countryCode = e.children("A").eq(0).attr("data-country-code"),
                    countryName = e.children("A").eq(0).attr("data-country-name");
                app.europeMap.getDataFromApi(countryName, countryCode);
            }
        });
    },

    getDataFromApi: function (countryName, countryCode) {
        var dataPackage = {'countryName': countryName, 'countryCode': countryCode};
        $.ajax({
            url: '/api/get-news',
            method: 'GET',
            data: dataPackage,
            dataType: 'json',
            success: function(response) {
                var package = JSON.parse(response.answer);
                var news = package.articles;
                app.europeMap.listNews(news);
            },
            error: function() {
                console.log('ERROR: API calling failed.');
            }
        });
    },

    listNews: function (news) {

    }

};

$(document).ready(function(){
    app.init();
});