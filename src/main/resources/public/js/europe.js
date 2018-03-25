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
                app.europeMap.listNews(news, countryName);
            },
            error: function() {
                console.log('ERROR: API calling failed.');
            }
        });
    },

    listNews: function (news, countryName) {
        console.log(countryName);
        var modalTitle = document.getElementById("newsModalLabel");
        modalTitle.textContent = "News about " + countryName;

        // delete previous news table content:
        var deleteNewsRows = document.getElementsByClassName('news-table-row');
        while (deleteNewsRows.length > 0) {
            deleteNewsRows[0].remove();
        }

        // put data into the modal:
        var newsTable = document.getElementById('news-table-body');
        for (newsIndex = 0; newsIndex < news.length; newsIndex++) {

            var newRow = document.createElement('tr');
            newRow.className = 'news-table-row';

            var title = document.createElement('td');
            var titleText = document.createTextNode(news[newsIndex].title);
            title.appendChild(titleText);

            var author = document.createElement('td');
            var authorText = document.createTextNode(news[newsIndex].author);
            author.appendChild(authorText);

            var link = document.createElement('td');
            var anchor = document.createElement('a');
            document.createElement('href');
            var linkText = document.createTextNode("Read");
            anchor.appendChild(linkText);
            anchor.target = "_blank";
            anchor.title = "Read";
            anchor.href = news[newsIndex].url;
            link.appendChild(anchor);

            // todo: add picture

            newRow.appendChild(title);
            newRow.appendChild(author);
            newRow.appendChild(link);

            newsTable.appendChild(newRow);
        }
    }

};

$(document).ready(function(){
    app.init();
});