This is an Android demo application for searching articles in Newyork times articles

Time spent: 15 hours spent in total

Completed user stories:


    Rquired: User can enter a search query that will display a grid of news articles using the thumbnail and headline from the New York Times Search API. (3 points)
    Rquired:  User can click on "settings" which allows selection of advanced search options to filter results. (3 points)
    Rquired:  User can configure advanced search filters such as: (points included above)
        Begin Date (using a date picker)
        News desk values (Arts, Fashion & Style, Sports)
        Sort order (oldest or newest)
    Rquired:  Subsequent searches will have any filters applied to the search results. (1 point) - Working*
    Rquired:  User can tap on any article in results to view the contents in an embedded browser. (2 points)
    Rquired:  User can scroll down "infinitely" to continue loading more news articles. The maximum number of articles is limited by the API search. (1 point)

    Optional: Advanced: Robust error handling, check if internet is available, handle error cases, network failures. (1 point)
    Optional: Advanced: Use the ActionBar SearchView or custom layout as the query box instead of an EditText. (1 point)
    Optional:  Advanced: Replace Filter Settings Activity with a lightweight modal overlay. (2 points)
    
    Optional:  Bonus: Use the RecyclerView with the StaggeredGridLayoutManager to display improve the grid of image results (see Picasso guide too). (2 points)


![alt tag](https://github.com/nigamg/NYArticleSearchClient/blob/master/app/src/main/res/nysearch_client_V1.gif)
