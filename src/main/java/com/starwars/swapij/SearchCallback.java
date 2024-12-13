package com.starwars.swapij;

@FunctionalInterface
interface SearchCallback {
    void onResult(Result<SearchResult> result);
}
