package me.ssiddh.lunchables.data.models

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class PlaceResultTest {

    private val validPlaceResultString = """
        {
            "business_status": "OPERATIONAL",
            "geometry": {
                "location": {
                    "lat": -33.864458,
                    "lng": 151.2062952
                },
                "viewport": {
                    "northeast": {
                        "lat": -33.8630140197085,
                        "lng": 151.2076772302915
                    },
                    "southwest": {
                        "lat": -33.8657119802915,
                        "lng": 151.2049792697085
                    }
                }
            },
            "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/lodging-71.png",
            "name": "Amora Hotel",
            "photos": [
                {
                    "height": 608,
                    "html_attributions": [
                        "<a href=\"https://maps.google.com/maps/contrib/112581335421444049471\">Amora Hotel Jamison Sydney</a>"
                    ],
                    "photo_reference": "ATtYBwKm98cENJYpOqZIaEkQh5Ikfz2u5gabDaJJQ1PxKQhVPcfT_cMbXnfCV1dX-gElED8SHmMJDd69Qm6In4_J7Dzl3M8Vfes5MnweEsdyn3r_UlzBsiXSzYQoR0cYUEHdDFHNTdY51JlItxtDrVi0HCaaoKhjdH6LyLRD9aB31yjw5q07",
                    "width": 1080
                }
            ],
            "place_id": "ChIJa51FEUGuEmsRIXRtDjLFQXM",
            "plus_code": {
                "compound_code": "46P4+6G Sydney NSW, Australia",
                "global_code": "4RRH46P4+6G"
            },
            "price_level": 3,
            "rating": 4.3,
            "reference": "ChIJa51FEUGuEmsRIXRtDjLFQXM",
            "scope": "GOOGLE",
            "types": [
                "spa",
                "lodging",
                "bar",
                "restaurant",
                "food",
                "point_of_interest",
                "establishment"
            ],
            "user_ratings_total": 1794,
            "vicinity": "11 Jamison Street, Sydney"
        }
    """.trimIndent()

    private val invalidPlaceResult = """
        {
            "business_status": "OPERATIONAL",
            "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/lodging-71.png",
            "name": "Amora Hotel",
            "photos": [
                {
                    "height": 608,
                    "html_attributions": [
                        "<a href=\"https://maps.google.com/maps/contrib/112581335421444049471\">Amora Hotel Jamison Sydney</a>"
                    ],
                    "photo_reference": "ATtYBwKm98cENJYpOqZIaEkQh5Ikfz2u5gabDaJJQ1PxKQhVPcfT_cMbXnfCV1dX-gElED8SHmMJDd69Qm6In4_J7Dzl3M8Vfes5MnweEsdyn3r_UlzBsiXSzYQoR0cYUEHdDFHNTdY51JlItxtDrVi0HCaaoKhjdH6LyLRD9aB31yjw5q07",
                    "width": 1080
                }
            ],
            "plus_code": {
                "compound_code": "46P4+6G Sydney NSW, Australia",
                "global_code": "4RRH46P4+6G"
            },
            "reference": "ChIJa51FEUGuEmsRIXRtDjLFQXM",
            "vicinity": "11 Jamison Street, Sydney"
        }
    """.trimIndent()

    private lateinit var jsonAdapter: JsonAdapter<PlaceResult>

    @Before
    fun setup() {
        jsonAdapter = Moshi.Builder().addLast(KotlinJsonAdapterFactory())
            .build().adapter(PlaceResult::class.java)
    }

    @Test
    fun `Should be able to parse a valid place result json`() {
        val placeResult = jsonAdapter.fromJson(validPlaceResultString)

        assertNotNull(placeResult)
        assertEquals(placeResult?.businessStatus, BusinessStatus.OPERATIONAL)
    }

    @Test(expected = JsonDataException::class)
    fun `Should result in exception when invalid json`() {
        val placeResult = jsonAdapter.fromJson(invalidPlaceResult)
    }
}
