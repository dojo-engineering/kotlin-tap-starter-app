package com.example.sampleposapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.fragment.app.Fragment
import com.example.sampleposapp.databinding.FragmentPayBinding


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class PaymentFragment : Fragment() {

    private var _binding: FragmentPayBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPayBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val displayToast = fun(message: String) {
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(context, message, duration) // in Activity
            toast.show()
        }

        val handleSaleResponse = registerForActivityResult(StartActivityForResult()) { result ->
            val saleStatus = result.data?.getStringExtra("com.dojo.extra.SALE_STATUS")
            val baseAmount = result.data?.getIntExtra("com.dojo.extra.BASE_AMOUNT", -1)
            val gratuityAmount = result.data?.getIntExtra("com.dojo.extra.GRATUITY_AMOUNT", 0)
            val txResult = result.data?.getStringExtra("com.dojo.extra.TRANSACTION_RESULT")
            displayToast("Payment for $baseAmount with gratuity of $gratuityAmount was $txResult and $saleStatus")
        }

        binding.pay.setOnClickListener {
            try {
                val saleIntent = Intent("com.dojo.action.SALE")
                saleIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                saleIntent.putExtra("com.dojo.extra.BASE_AMOUNT", 1000)
                handleSaleResponse.launch(saleIntent)
            } catch (exception: Exception) {
                displayToast("Error launching the Tap SALE intent")
                println("Error launching activity $exception")
            }
        }

        val handleInitResponse = registerForActivityResult(StartActivityForResult()) { result ->
            println(result);
            if (result.resultCode == Activity.RESULT_OK) {
                displayToast("Successfully initialised TaP")
            } else {
                displayToast("Failed to initialise TaP")
            }
        }

        binding.initialise.setOnClickListener {
            try {
                val initIntent = Intent("com.dojo.action.INIT")
                initIntent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                handleInitResponse.launch(initIntent);
            } catch (exception: Exception) {
                displayToast("Error launching the Tap INIT intent")
                println("Error launching activity $exception")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}