package xyz.blueowl.ispychallenge.api.service

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import xyz.blueowl.ispychallenge.api.models.APIChallenge
import xyz.blueowl.ispychallenge.api.models.APIUser
import java.io.InputStreamReader

/**
 * Implementation of the API Service that mocks the necessary data by reading a json file and
 * converting it into API models and returning them via callback.
 *
 * This class is specifically make to mock the "Network layer" and shouldn't necessarily be used in
 * the Tech Challenge.
 */
class MockAPIService(
    private val usersReader: InputStreamReader,
    private val challengesReader: InputStreamReader
): APIService {

    private val gson = Gson()

    override fun getUsers(completionCallback: (List<APIUser>) -> Unit) {
        val users: List<APIUser> = gson.fromJson(usersReader, object: TypeToken<List<APIUser>>() {}.type)
        completionCallback.invoke(users)
    }

    override fun getChallenges(completionCallback: (List<APIChallenge>) -> Unit) {
        val challenges: List<APIChallenge> = gson.fromJson(challengesReader, object: TypeToken<List<APIChallenge>>() {}.type)
        completionCallback.invoke(challenges)
    }
}