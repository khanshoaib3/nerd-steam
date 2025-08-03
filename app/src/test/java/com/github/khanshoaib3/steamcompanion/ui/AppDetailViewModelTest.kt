package com.github.khanshoaib3.steamcompanion.ui

import android.content.Context
import com.github.khanshoaib3.steamcompanion.data.model.api.Platforms
import com.github.khanshoaib3.steamcompanion.data.model.appdetail.Category
import com.github.khanshoaib3.steamcompanion.data.model.appdetail.CommonAppDetails
import com.github.khanshoaib3.steamcompanion.data.model.appdetail.Media
import com.github.khanshoaib3.steamcompanion.data.model.appdetail.Requirement
import com.github.khanshoaib3.steamcompanion.data.repository.LocalBookmarkRepository
import com.github.khanshoaib3.steamcompanion.data.repository.OnlineAppDetailRepository
import com.github.khanshoaib3.steamcompanion.data.repository.OnlineIsThereAnyDealRepository
import com.github.khanshoaib3.steamcompanion.di.AppModule
import com.github.khanshoaib3.steamcompanion.ui.screen.appdetail.AppDetailViewModel
import com.github.khanshoaib3.steamcompanion.ui.utils.Route
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock

class AppDetailViewModelTest {
    private lateinit var context: Context
    private val appModule = AppModule()
    private lateinit var viewmodel: AppDetailViewModel

    @Before
    fun initialize() {
        context = mock<Context> { }

        viewmodel = AppDetailViewModel(
            appDetailRepository = OnlineAppDetailRepository(
                steamInternalWebApiService = appModule.provideSteamInternalWebApiService(),
                priceTrackingDao = appModule.providePriceTrackingDao(context)
            ),
            isThereAnyDealRepository = OnlineIsThereAnyDealRepository(appModule.provideIsThereAnyDealApiService()),
            bookmarkRepository = LocalBookmarkRepository(appModule.provideBookmarkDao(context)),
            key = Route.AppDetail(appId = 220)
        )

    }

    @Test
    fun `Test fetching of price info from IsThereAnyDeal`() {
        runBlocking {
            viewmodel.fetchISTDPriceInfo()
            viewmodel.collatedAppData.value.let {
                println(it.isThereAnyDealPriceInfo)
            }
        }
    }

    @Test
    fun `Test collating steam and ITAD responses`() {
        runBlocking {
            viewmodel.fetchCommonAppDetails()
            println(viewmodel.collatedAppData.value.commonDetails)
        }
    }

    private val example = CommonAppDetails(
        name = "Half - Life 2",
        type = "game",
        imageUrl = "",
        description = """Reawakened from stasis in the occupied metropolis of City 17, Gordon Freeman is joined by Alyx Vance as he leads a desperate human resistance. Experience the landmark first-person shooter packed with immersive world-building, boundary-pushing physics, and exhilarating combat.,""",
        about = """< p class = \"bb_paragraph\" > < i > The Seven Hour War is lost.Earth has surrendered. The Black Mesa incident is a distant memory.</i> The player again picks up the crowbar of research scientist Gordon Freeman, who finds himself on an alien-infested Earth being picked to the bone, its resources depleted, its populace dwindling. Freeman is thrust into the unenviable role of rescuing the world from the wrong he unleashed back at Black Mesa. And a lot of people he cares about are counting on him.</p><p class=\"bb_paragraph\">Half-Life 2 is the landmark first-person shooter that “forged the framework for the next generation of games” (PC Gamer). Experience a thrilling campaign packed with unprecedented levels of immersive world-building, boundary-pushing physics, and exhilarating combat.</p><p class=\"bb_paragraph\"> </p><h2 class=\"bb_tag\">Includes the Half-Life 2 Episode One and Two Expansions</h2><p class="bb_paragraph">The story of Half-Life 2 continues with Episodes One and Two, full-featured Half-Life adventures set during the aftermath of the base game. They are accessible from the main menu, and you will automatically advance to the next episode after completing each one.</p><p class="bb_paragraph"><div class="bb_img_ctn"><img class="bb_img" src="https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/220/extras/363ed9ea180fe9d548521b2cc18d8c22.avif?t=1745368545" data -has-big /></div></p><h2 class="bb_tag"> Half-Life 2: Deathmatch</h2><p class = "bb_paragraph" > Fast multiplayer action set in the Half - Life 2 universe!HL2's physics adds a new dimension to deathmatch play. Play straight deathmatch or try Combine vs. Resistance teamplay. Toss a toilet at your friend today! <i>Included in your Steam library with purchase of Half-Life 2.</i></p><p class="bb_paragraph"><div class="bb_img_ctn"><img class="bb_img" src="https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/220/extras/7297f35bf74ad59802e5da47afb783cd.avif?t=1745368545" data-has-big /></div></p><h2 class="bb_tag">Steam Workshop</h2><p class="bb_paragraph">Play entire campaigns or replace weapons, enemies, UI, and more with content created by the community.</p>""",
        releaseDate = "16 Nov, 2004",
        isReleased = true,
        developers = listOf("Valve"),
        publishers = listOf("Valve"),
        platforms = Platforms(windows = true, mac = false, linux = true),
        websiteUrl = "http://www.half-life.com/halflife2",
        isFree = false,
        currentPrice = 480.0f,
        originalPrice = 480.0f,
        currency = "INR",
        tags = listOf("FPS", "Action", "Sci-fi", "Classic", "Singleplayer"),
        categories = listOf(
            Category(
                name = "Single - player",
                url = "https://steamdb.info/static/img/categories/2.png"
            ),
            Category(
                name = "Steam Achievements",
                url = "https://steamdb.info/static/img/categories/22.png"
            )
        ),
        requirements = listOf(
            Requirement(
                platform = "Windows",
                minimumRequirements = """< strong > Minimum : < / strong ><br> < ul class = "bb_ul" > < li ><strong> OS *:</strong> Windows 7, Vista, XP<br></li><li><strong>Processor:</strong> 1.7 Ghz<br></li><li><strong>Memory:</strong> 512 MB RAM<br></li><li><strong>Graphics:</strong> DirectX 8.1 level Graphics Card (requires support for SSE)<br></li><li><strong>Storage:</strong> 6500 MB available space</li></ul>""",
                recommendedRequirements = null
            )
        ),
        reviews = listOf(),
        media = Media(
            screenshots = listOf(
                "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/220/ss_47b4105b396de408cb8b6b4f358c69e5e2a62dae.1920x1080.jpg?t=1745368545",
                "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/220/ss_0e499071a60a20b24149ad65a8edb769250f2921.1920x1080.jpg?t=1745368545"
            ),
            movies = listOf(
                "http://video.akamai.steamstatic.com/store_trailers/257074217/movie480_vp9.webm?t=1732069594",
                "http://video.akamai.steamstatic.com/store_trailers/904/movie480_vp9.webm?t=1732069598"
            )
        )
    )
}
