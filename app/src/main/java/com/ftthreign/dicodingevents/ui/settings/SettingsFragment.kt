package com.ftthreign.dicodingevents.ui.settings

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.ftthreign.dicodingevents.R
import com.ftthreign.dicodingevents.databinding.FragmentSettingsBinding
import com.ftthreign.dicodingevents.ui.viewModels.MainViewModel
import com.ftthreign.dicodingevents.ui.viewModels.ViewModelFactory

class SettingsFragment : PreferenceFragmentCompat() {
    private var _binding : FragmentSettingsBinding? = null
    private val binding get() = _binding
    private val viewModel by viewModels<MainViewModel>{
        ViewModelFactory.getInstance(requireActivity())
    }

//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        return FragmentSettingsBinding.inflate(inflater, container, false).root
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val themeButton = findPreference<SwitchPreference>("themeKey")

        viewModel.getThemeSettings().observe(viewLifecycleOwner) {isDark ->
            themeButton?.isChecked = isDark
        }

        themeButton?.setOnPreferenceChangeListener { _, newValue ->
            val isDarkMode = newValue as Boolean
            viewModel.saveThemeSetting(isDarkMode)
            setTheme(isDarkMode)
            true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding =null
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preference)
    }

    private fun setTheme(isDark : Boolean) {
        if (isDark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

}