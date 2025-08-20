# NerdSteam

**NerdSteam** is a hobby project of mine where people can view trending and top games of [Steam](https://store.steampowered.com/),
compare prices from other platforms, see historic prices, player statistics, etc.
The app is built using Kotlin + Jetpack Compose following MVVM architecture with a clean and responsive UI.

## Media

## Features

### Game Discovery
- **Trending Games**: View currently trending PC games on Steam (compared to last hour)
- **Top Games**: Browse top 10 games with highest current player counts
- **Game Details**: Comprehensive game information including developers, publishers, ratings, descriptions, and current pricing

### Price Intelligence
- **Multi-Platform Price Comparison**: Track game prices across various 3rd party platforms
- **Price Alerts**: Set custom price alerts for games you're interested in
- **Deal Tracking**: Stay updated on the best available deals

### Analytics & Insights
- **Player Statistics**: Monthly player count data from game release to present
- **Historical Trends**: Visualize game popularity over time
- **Bookmarking**: Save and organize your favorite games

## Technical Stack

### Frontend
- **Kotlin** - Primary programming language
- **Jetpack Compose** - Modern declarative UI toolkit
- **Material 3 Expressive** - Latest design system implementation
- **Navigation 3 Alpha** - Cutting-edge navigation architecture

### Architecture & Libraries
- **Retrofit** - Type-safe HTTP client for Steam and IsThereAnyDeal APIs
- **Room Database** - Local data persistence and caching
- **Scrape-it** - Web scraping for SteamCharts data
- **MVVM Pattern** - Clean separation of concerns

### Data Sources
- **Steam API** - Game details and metadata
- **SteamCharts.com** - Player statistics and trending data
- **IsThereAnyDeal.com** - Price comparison across platforms

## Architecture Implementation

### Intelligent Caching System
- **Hourly Cache**: SteamCharts data cached per hour for optimal freshness
- **Daily Cache**: Game details cached for 24 hours to reduce API calls
- **Room Database**: Efficient local storage with automatic cache invalidation

### Data Flow
```
Steam API ──────► Retrofit ──────► Repository
                                       │
IsThereAnyDeal ──► Retrofit ───────────┤
                                       │
SteamCharts ─────► Scrape-it ──────────┤
                                       ▼
                                  Room Cache
                                       │
                                       ▼
                                   UI Layer
                               (Jetpack Compose)
```

### Performance Optimizations
- **Smart Caching**: Reduces redundant network requests while maintaining data freshness
- **Efficient Scraping**: Optimized web scraping with minimal resource usage
- **Modern Navigation**: Latest Navigation 3 Alpha for smooth transitions

## Technical Highlights

This project demonstrates:
- **Bleeding-edge Android Development**: Implementation of alpha and experimental libraries
- **Clean Architecture**: Repository pattern with proper separation of concerns
- **Advanced Data Management**: Multi-source data aggregation with intelligent caching
- **Modern UI Patterns**: Material 3 Expressive with responsive design
- **Performance Engineering**: Optimized network usage and local storage strategies


## Project Setup

Add your IsThereAnyDeal API key to `secret.properties`:
```
IS_THERE_ANY_DEAL_API_KEY="<your_api_key_here>"
```

## Credits & Resources

- **[SteamCharts](https://steamcharts.com/)** - for player statistics.
- **[IsThereAnyDeal](https://isthereanydeal.com/)** - for price deals and historical statistics.
- 

```

