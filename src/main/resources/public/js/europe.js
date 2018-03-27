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
                    countryCode = e.children("A").eq(0).attr("data-country-code");
                var countryName = link.substr(1);
                $('#newsModal').modal('toggle');
                app.europeMap.getDataFromApi(countryName, countryCode);
            }
        });
    },

    getDataFromApi: function (countryName, countryCode) {
        var dataPackage = {'countryName': countryName, 'countryCode': countryCode};
        $.ajax({
            url: '/api/get-news',
            method: 'POST',
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(dataPackage),
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

        // sorting news by date, descending:
        news.sort(function(a, b){
            var x = a.publishedAt;
            var y = b.publishedAt;
            if (x > y) {return -1;}
            if (x < y) {return 1;}
            return 0;
        });

        var modalTitle = document.getElementById("newsModalLabel");
        countryName.charAt(0).toUpperCase();
        modalTitle.textContent = "News about " + countryName.charAt(0).toUpperCase() + countryName.substr(1);

        // delete previous news-table content in the modal:
        var deleteNewsRows = document.getElementsByClassName('news-table-row');
        while (deleteNewsRows.length > 0) {
            deleteNewsRows[0].remove();
        }

        // put data into the table row by row:
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

            var date = document.createElement('td');
            var dateWithoutTime = news[newsIndex].publishedAt.substr(0, 10);
            var dateText = document.createTextNode(dateWithoutTime);
            date.appendChild(dateText);

            var image = document.createElement('img');
            image.setAttribute('class', 'image-in-modal');
            image.setAttribute('src', news[newsIndex].urlToImage);

            newRow.appendChild(title);
            newRow.appendChild(author);
            newRow.appendChild(link);
            newRow.appendChild(date);
            newRow.appendChild(image);

            newsTable.appendChild(newRow);
        }
    }

};

$(document).ready(function(){
    app.init();
});