package xyz.blueowl.ispychallenge.ui.data_browser.challenges

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import xyz.blueowl.ispychallenge.databinding.FragmentDataBrowserBinding
import xyz.blueowl.ispychallenge.extensions.requireISpyApplication
import xyz.blueowl.ispychallenge.ui.data_browser.shared.DataBrowserNavState
import xyz.blueowl.ispychallenge.ui.data_browser.shared.GenericSingleViewModelFactory
import xyz.blueowl.ispychallenge.ui.data_browser.shared.UniversalListAdapter
import xyz.blueowl.ispychallenge.ui.safeCollectFlow

class ChallengesFragment: Fragment() {

    private var _binding: FragmentDataBrowserBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val userId = arguments?.let {
            val params = ChallengesFragmentArgs.fromBundle(it)
            params.userId
        } ?: throw IllegalArgumentException("Missing user id")

        val challengesAdapter = UniversalListAdapter()

        val factory = GenericSingleViewModelFactory (ChallengesViewModel::class.java) {
            ChallengesViewModel(userId, requireISpyApplication().dataRepository)
        }
        val viewModel = ViewModelProvider(this, factory)[ChallengesViewModel::class.java]
        _binding = FragmentDataBrowserBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.recyclerViewDataBrowser.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = challengesAdapter
        }

        safeCollectFlow(viewModel.navigationFlow) { navState ->
            val action = when (navState) {
                is DataBrowserNavState.ChallengeNavState -> {
                    ChallengesFragmentDirections.actionNavigationChallengesToNavigationChallenge(navState.challengeId)
                }
                else -> throw IllegalArgumentException("Cannot support nav state $navState")
            }

            findNavController().navigate(action)
        }

        safeCollectFlow(viewModel.adapterItems) {
            challengesAdapter.submitList(it)
        }

        return root
    }
}