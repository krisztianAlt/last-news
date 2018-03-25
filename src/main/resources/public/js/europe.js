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
                console.log(countryName + ' ' + countryCode);
            }
        });


    }

};

$(document).ready(function(){
    app.init();
});