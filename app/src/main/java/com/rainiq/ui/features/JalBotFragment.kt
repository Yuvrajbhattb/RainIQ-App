package com.rainiq.ui.features

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.rainiq.BuildConfig
import com.rainiq.R
import com.rainiq.data.preferences.UserPreferences
import com.rainiq.databinding.FragmentFeatureJalbotBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

/**
 * JalBotFragment — AI-powered rainwater harvesting assistant.
 * Uses Gemini API to answer questions with the user's roof context.
 *
 * Designed and Developed by Yuvraj Bhatt
 */
class JalBotFragment : Fragment() {

    private var _binding: FragmentFeatureJalbotBinding? = null
    private val binding get() = _binding!!

    private val chatHistory = mutableListOf<Pair<String, String>>() // role, text

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeatureJalbotBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Show welcome message
        addBotBubble("Hi! I'm Jal-Bot 💧 Ask me anything about rainwater harvesting, your roof setup, or how to save more water.")

        // Send button
        binding.btnSend.setOnClickListener { sendMessage() }

        // Keyboard "Send" action
        binding.etChatInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                sendMessage()
                true
            } else false
        }
    }

    private fun sendMessage() {
        val text = _binding?.etChatInput?.text?.toString()?.trim() ?: return
        if (text.isEmpty()) return

        _binding?.etChatInput?.setText("")
        addUserBubble(text)

        // Show typing indicator
        val typingView = addBotBubble("Thinking... 💭")

        viewLifecycleOwner.lifecycleScope.launch {
            val reply = callGeminiApi(text)
            // Remove typing indicator and show real reply
            if (_binding != null) {
                _binding?.chatContainer?.removeView(typingView)
                addBotBubble(reply)
                scrollToBottom()
            }
        }
    }

    // ─── Bubble Builders ──────────────────────────────────────────────

    private fun addUserBubble(text: String): View {
        val ctx = requireContext()
        val density = ctx.resources.displayMetrics.density

        val row = LinearLayout(ctx).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.BOTTOM
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { bottomMargin = (16 * density).toInt() }
        }

        // Left spacer
        row.addView(View(ctx).apply {
            layoutParams = LinearLayout.LayoutParams((48 * density).toInt(), 0)
        })

        // Message bubble
        val bubble = FrameLayout(ctx).apply {
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f).apply {
                marginStart = (48 * density).toInt()
            }
            setBackgroundResource(R.drawable.bg_glass_card)
            setPadding(
                (16 * density).toInt(), (16 * density).toInt(),
                (16 * density).toInt(), (16 * density).toInt()
            )
        }

        val tv = TextView(ctx).apply {
            this.text = text
            setTextColor(ctx.getColor(R.color.text_primary))
            textSize = 14f
            setLineSpacing((3 * density), 1f)
        }
        bubble.addView(tv)
        row.addView(bubble)

        binding.chatContainer.addView(row)
        scrollToBottom()
        return row
    }

    private fun addBotBubble(text: String): View {
        val ctx = requireContext()
        val density = ctx.resources.displayMetrics.density

        val row = LinearLayout(ctx).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.BOTTOM
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { bottomMargin = (16 * density).toInt() }
        }

        // Bot avatar
        val avatarFrame = FrameLayout(ctx).apply {
            layoutParams = LinearLayout.LayoutParams(
                (36 * density).toInt(), (36 * density).toInt()
            ).apply { marginEnd = (10 * density).toInt() }
            setBackgroundResource(R.drawable.bg_glass_chip)
        }
        val icon = ImageView(ctx).apply {
            layoutParams = FrameLayout.LayoutParams(
                (18 * density).toInt(), (18 * density).toInt()
            ).apply { gravity = Gravity.CENTER }
            setImageResource(R.drawable.ic_sparkle)
            setColorFilter(ctx.getColor(R.color.accent_teal))
        }
        avatarFrame.addView(icon)
        row.addView(avatarFrame)

        // Message bubble
        val bubble = FrameLayout(ctx).apply {
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f).apply {
                marginEnd = (48 * density).toInt()
            }
            setBackgroundResource(R.drawable.bg_glass_ai_card)
            setPadding(
                (16 * density).toInt(), (16 * density).toInt(),
                (16 * density).toInt(), (16 * density).toInt()
            )
        }

        val tv = TextView(ctx).apply {
            this.text = text
            setTextColor(ctx.getColor(R.color.text_primary))
            textSize = 14f
            setLineSpacing((3 * density), 1f)
        }
        bubble.addView(tv)
        row.addView(bubble)

        binding.chatContainer.addView(row)
        scrollToBottom()
        return row
    }

    private fun scrollToBottom() {
        _binding?.scrollChat?.post {
            _binding?.scrollChat?.fullScroll(ScrollView.FOCUS_DOWN)
        }
    }

    // ─── Gemini API ───────────────────────────────────────────────────

    private suspend fun callGeminiApi(userMessage: String): String {
        // Capture context on main thread before switching to IO
        val ctx = context ?: return "⚠️ Error: screen not available."
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isBlank() || apiKey == "your_gemini_key_here") {
            return "⚠️ No Gemini API key found. Please add your key to local.properties as GEMINI_API_KEY=your_key"
        }

        val prefs = UserPreferences(ctx)
        val systemPrompt = buildSystemPrompt(prefs)

        // Add user message to history
        chatHistory.add("user" to userMessage)

        return withContext(Dispatchers.IO) {
            try {
                // Build contents array with chat history
                val contentsArray = JSONArray()
                for ((role, text) in chatHistory) {
                    contentsArray.put(JSONObject().apply {
                        put("role", role)
                        put("parts", JSONArray().put(JSONObject().put("text", text)))
                    })
                }

                val requestBody = JSONObject().apply {
                    put("contents", contentsArray)
                    put("systemInstruction", JSONObject().apply {
                        put("parts", JSONArray().put(JSONObject().put("text", systemPrompt)))
                    })
                    put("generationConfig", JSONObject().apply {
                        put("temperature", 0.7)
                        put("maxOutputTokens", 512)
                    })
                }

                val url = URL(
                    "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=$apiKey"
                )
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.setRequestProperty("Content-Type", "application/json")
                conn.doOutput = true
                conn.connectTimeout = 15000
                conn.readTimeout = 30000

                OutputStreamWriter(conn.outputStream).use { it.write(requestBody.toString()) }

                val responseCode = conn.responseCode
                val stream = if (responseCode in 200..299) conn.inputStream else conn.errorStream
                val responseText = BufferedReader(InputStreamReader(stream)).use { it.readText() }
                conn.disconnect()

                if (responseCode !in 200..299) {
                    chatHistory.removeLastOrNull()
                    // Parse the actual error message from Google's response
                    val errorMsg = try {
                        val errJson = JSONObject(responseText)
                        errJson.optJSONObject("error")?.optString("message") ?: responseText.take(200)
                    } catch (_: Exception) { responseText.take(200) }
                    return@withContext "⚠️ API error ($responseCode): $errorMsg"
                }

                val json = JSONObject(responseText)
                val candidates = json.optJSONArray("candidates")
                val reply = candidates
                    ?.optJSONObject(0)
                    ?.optJSONObject("content")
                    ?.optJSONArray("parts")
                    ?.optJSONObject(0)
                    ?.optString("text", "I couldn't generate a response. Please try again.")
                    ?: "I couldn't generate a response. Please try again."

                // Store bot reply in history for context
                chatHistory.add("model" to reply)
                reply
            } catch (e: Exception) {
                e.printStackTrace()
                chatHistory.removeLastOrNull()
                "⚠️ Connection error: ${e.localizedMessage ?: "Unknown error"}. Please check your internet and try again."
            }
        }
    }

    private fun buildSystemPrompt(prefs: UserPreferences): String {
        val roofArea = prefs.roofArea
        val tankCap = prefs.tankCapacity
        val material = prefs.roofMaterial
        val runoff = prefs.runoffCoefficient
        val city = prefs.city
        val goal = prefs.monthlyGoalLiters

        return """
You are Jal-Bot, a friendly and knowledgeable AI assistant for the RainIQ app — a rainwater harvesting tracker.

User's setup:
- Roof area: ${roofArea} sq ft
- Tank capacity: ${tankCap} liters
- Roof material: $material (runoff coefficient: $runoff)
- City: $city
- Monthly goal: ${goal.toInt()} liters

Your expertise:
- Rainwater harvesting calculations and best practices
- Water conservation tips specific to their city and climate
- Explaining the formula: Liters = Area(sqft) × 0.0929 × Rainfall(mm) × Runoff × 1000
- Tank maintenance and water quality
- Cost savings and environmental impact

Rules:
- Keep answers concise (2-4 short paragraphs max)
- Use emoji sparingly for warmth 💧🌧️🌱
- Always relate advice to their specific roof setup when relevant
- If asked about non-water topics, gently redirect to water conservation
- Use metric units (liters, mm) by default
        """.trimIndent()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
