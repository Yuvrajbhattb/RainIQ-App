# RainIQ / Jal-Sanchay Tracker UI Design System

**Document type:** Implementation-accurate Android UI design system  
**Version:** 2.0  
**Date:** April 30, 2026  
**Source of truth:** `app/src/main/res/values`, `app/src/main/res/drawable`, `app/src/main/res/layout`, `app/src/main/java/com/rainiq/components`  
**Design language:** Liquid Nature Glass

---

## 1. Design Philosophy

RainIQ uses a dark, premium nature interface built from translucent green-black glass over a photographic or radial forest background. The UI must feel like layered wet glass: transparent enough to reveal the background, but edged with visible white specular rims and teal/lime environmental accents.

The design is not a generic dark mode. Every neutral surface is green-biased, every glass object has a visible rim, and water/intelligence states are teal while achievement/reward/action states are lime.

---

## 2. Color System

All tokens below are exact Android values from `app/src/main/res/values/colors.xml`. Android transparent colors use `#AARRGGBB`.

### 2.1 Background Colors

| Token | Hex | Usage |
|---|---:|---|
| `bg_deepest` | `#0A150A` | Root background behind photos and system fallback |
| `bg_dark` | `#111D0F` | Main dark surface and Material surface |
| `bg_forest` | `#152010` | Bottom sheet and forest card tint |
| `bg_mid` | `#1C2B18` | Secondary Material container |

### 2.2 Glass Surface Tokens

| Token | Hex / ARGB | Opacity | Usage |
|---|---:|---:|---|
| `glass_primary` | `#6E1C3519` | 43% | Main liquid glass cards |
| `glass_secondary` | `#551A3018` | 33% | Secondary and nested dark glass |
| `glass_nav` | `#991A2E1A` | 60% | Floating bottom navigation pill |
| `glass_button` | `#59263520` | 35% | Header icon buttons and glass orbs |
| `glass_chip` | `#552B3D22` | 33% | Chips, badges, icon containers |
| `glass_tooltip` | `#EBFFFFFF` | 92% | Light chart tooltip glass |
| `glass_overlay` | `#47000000` | 28% | Full-screen black photo overlay |
| `glass_green_tint` | `#3A0A1408` | 23% | Full-screen green tint overlay |
| `glass_teal_tint` | `#144ECDC4` | 8% | Teal water reflection tint |
| `glass_water_card` | `#1A4ECDC4` | 10% | Hero water card tint |

### 2.3 Specular And Rim Tokens

| Token | Hex / ARGB | Opacity | Usage |
|---|---:|---:|---|
| `glass_specular_top` | `#55FFFFFF` | 33% | Top-edge prism highlight |
| `glass_specular_mid` | `#2EFFFFFF` | 18% | Mid stop for glass highlight gradients |
| `glass_rim_outer` | `#45FFFFFF` | 27% | Primary visible white glass rim |
| `glass_rim_inner` | `#18000000` | 10% | Inner/offset shadow for convex depth |
| `glass_nav_circle` | `#4D1E3017` | 30% | Convex bottom-nav icon body |

### 2.4 Accent Colors

| Token | Hex | Usage |
|---|---:|---|
| `accent_lime` | `#C8E632` | Primary CTA, reward, achievement, success |
| `accent_lime_dark` | `#9DB520` | Lime pressed state |
| `accent_orange` | `#E8692A` | Warning, alerts, notification dot |
| `accent_orange_dark` | `#C45520` | Orange pressed state |
| `accent_teal` | `#4ECDC4` | Water, flow, intelligence, AI |
| `accent_teal_dark` | `#35A89F` | Teal pressed state |
| `accent_red_soft` | `#E05555` | Error and destructive alerts |

### 2.5 Text Colors

| Token | Hex / ARGB | Usage |
|---|---:|---|
| `text_primary` | `#FFFFFF` | Headlines and primary labels |
| `text_secondary` | `#B8FFFFFF` | Secondary labels, descriptions |
| `text_tertiary` | `#73FFFFFF` | Hints, metadata, inactive labels |
| `text_accent_lime` | `#C8E632` | Achievement values |
| `text_accent_teal` | `#4ECDC4` | Water metric values |
| `text_accent_orange` | `#E8692A` | Warning values |
| `text_on_light_glass` | `#1A2E1A` | Text on lime CTA and light tooltip glass |

### 2.6 Borders, Inputs, States, Charts

| Token | Hex / ARGB | Usage |
|---|---:|---|
| `border_glass_light` | `#45FFFFFF` | 1dp primary glass border |
| `border_glass_dark` | `#22FFFFFF` | Secondary glass border |
| `border_inner` | `#2AFFFFFF` | Internal dividers |
| `border_active` | `#66C8E632` | Lime active/selected border |
| `border_teal_active` | `#664ECDC4` | Teal AI/water focused border |
| `ripple_white` | `#26FFFFFF` | Glass ripple overlay |
| `nav_icon_active_bg` | `#2EFFFFFF` | Active nav icon background |
| `nav_icon_inactive` | `#73FFFFFF` | Inactive nav icon |
| `input_bg` | `#14FFFFFF` | Glass input background |
| `input_border_default` | `#29FFFFFF` | Input default stroke |
| `input_border_focus` | `#99C8E632` | Lime focused input stroke |
| `input_border_focus_teal` | `#994ECDC4` | Teal focused input stroke |
| `input_border_error` | `#B3E8692A` | Error input stroke |
| `inner_card_bg` | `#22FFFFFF` | Nested white glass card body |
| `inner_card_border` | `#38FFFFFF` | Nested white glass border |
| `cta_shadow` | `#47C8E632` | Lime CTA glow token |
| `fab_shadow` | `#59C8E632` | FAB glow token |
| `chart_grid` | `#1AFFFFFF` | Chart grid lines |
| `chart_axis_text` | `#99FFFFFF` | Chart axis text |
| `chart_primary` | `#C8E632` | Primary lime data |
| `chart_secondary` | `#4ECDC4` | Secondary teal water data |
| `chart_warning` | `#E8692A` | Warning chart data |
| `insight_bg_tint` | `#1AC8E632` | Lime insight card tint |
| `water_card_tint` | `#144ECDC4` | Water metric card tint |
| `ai_card_tint` | `#0F4ECDC4` | Subtle AI card tint |
| `swipe_delete_bg` | `#CCE05555` | Delete swipe background |
| `progress_achievement` | `#C8E632` | Achievement progress |
| `progress_water` | `#4ECDC4` | Water progress |

---

## 3. Background System

### 3.1 Runtime Layer Stack

The main shell and reusable background stack use this exact order:

1. `ImageView` full screen, `scaleType="centerCrop"`, fallback `@drawable/gradient_bg_dark_green`.
2. `View` overlay using `@color/glass_overlay` (`#47000000`, 28% black).
3. `View` overlay using `@color/glass_green_tint` (`#3A0A1408`, 23% green-black).
4. Fragment content.
5. Header layer.
6. Floating bottom navigation pill.

### 3.2 Gradient Fallback

`@drawable/gradient_bg_dark_green` is a radial gradient:

| Property | Value |
|---|---|
| Type | `radial` |
| Center | `centerX="0.3"`, `centerY="0.2"` |
| Radius | `1200dp` |
| Start | `#1F3A18` |
| End | `#080E07` |

### 3.3 Top Fade

`@drawable/bg_gradient_top_fade` fades from `@color/bg_deepest` to transparent at `angle="270"`.

---

## 4. Typography System

### 4.1 Font Files

Primary UI typography uses SF Pro Display from local font files:

| Font resource | File |
|---|---|
| `@font/sf_pro_display_regular` | `sf_pro_display_regular.ttf` |
| `@font/sf_pro_display_medium` | `sf_pro_display_medium.ttf` |
| `@font/sf_pro_display_bold` | `sf_pro_display_bold.ttf` |

Inter remains available and is used in a few legacy/fallback places:

| Font resource | Files |
|---|---|
| `@font/inter` | XML family |
| `@font/inter_regular` | `inter_regular.ttf` |
| `@font/inter_medium` | `inter_medium.ttf` |
| `@font/inter_semibold` | `inter_semibold.ttf` |
| `@font/inter_bold` | `inter_bold.ttf` |
| `@font/inter_light` | `inter_light.ttf` |

### 4.2 Theme Text Styles

These are exact styles from `themes.xml`.

| Style | Size | Font | Color | Letter spacing | Line extra | Usage |
|---|---:|---|---|---:|---:|---|
| `TextStyle.AppName` | `22sp` | SF Pro Medium | `text_primary` | `-0.014` | `6sp` | App name style token |
| `TextStyle.LocationLabel` | `13sp` | SF Pro Regular | `text_secondary` | n/a | `5sp` | Location/subtitle label |
| `TextStyle.DisplayHero` | `52sp` | SF Pro Medium | `text_primary` | `-0.038` | `4sp` | Hero metric |
| `TextStyle.DisplayUnit` | `24sp` | SF Pro Medium | `text_primary` | `-0.042` | `6sp` | Hero unit |
| `TextStyle.CardTitle` | `16sp` | SF Pro Medium | `text_primary` | `-0.013` | `6sp` | Card headings |
| `TextStyle.CardSubtitle` | `12sp` | SF Pro Medium | `text_secondary` | `0.017` | `4sp` | Card subtitles |
| `TextStyle.BodyPrimary` | `14sp` | SF Pro Regular | `text_primary` | n/a | `6sp` | Body text |
| `TextStyle.BodySecondary` | `12sp` | SF Pro Regular | `text_secondary` | n/a | `5sp` | Secondary body |
| `TextStyle.StatNumber` | `28sp` | SF Pro Bold | `accent_lime` | `-0.018` | `6sp` | Large metric value |
| `TextStyle.StatUnit` | `13sp` | SF Pro Medium | `text_secondary` | n/a | `5sp` | Metric unit |
| `TextStyle.ChipLabel` | `11sp` | SF Pro Medium | `text_secondary` | `0.045` | `3sp` | Chip labels |
| `TextStyle.NavLabel` | `10sp` | SF Pro Medium | `text_tertiary` | `0.030` | `3sp` | Bottom nav labels |
| `TextStyle.Tooltip` | `12sp` | SF Pro Medium | `text_on_light_glass` | n/a | `4sp` | Tooltip main |
| `TextStyle.TooltipSub` | `10sp` | SF Pro Regular | `text_on_light_glass` | n/a | `4sp` | Tooltip secondary |
| `TextStyle.ButtonCta` | `14sp` | SF Pro Medium | `text_on_light_glass` | `0.094` | n/a | All-caps CTA |

### 4.3 Implemented Brand Header Type

The home header overrides the older app-name token for a larger two-tone mark:

| Element | Text | Size | Font | Color | Letter spacing |
|---|---|---:|---|---|---:|
| Brand part 1 | `Rain` | `39sp` | SF Pro Bold | `text_primary` | `-0.014` |
| Brand part 2 | `IQ` | `39sp` | SF Pro Bold | `accent_teal` | `-0.014` |
| Tagline | `CALCULATE • CONSERVE • IMPACT` | `7sp` | SF Pro Medium | `accent_teal` | `0.06` |
| Avatar fallback | Initials | `14sp` | Inter SemiBold | `accent_lime` | n/a |

---

## 5. Spacing, Radius, And Sizing

### 5.1 Spacing Tokens

| Token | Value |
|---|---:|
| `space_2` | `2dp` |
| `space_4` | `4dp` |
| `space_8` | `8dp` |
| `space_10` | `10dp` |
| `space_12` | `12dp` |
| `space_16` | `16dp` |
| `space_20` | `20dp` |
| `space_24` | `24dp` |
| `space_32` | `32dp` |
| `space_48` | `48dp` |
| `space_88` | `88dp` |

### 5.2 Layout Tokens

| Token | Value | Usage |
|---|---:|---|
| `screen_margin_horizontal` | `20dp` | Main screen side padding |
| `card_gap_horizontal` | `12dp` | Gap between paired cards |
| `content_top_below_header` | `16dp` | Top content spacing below header |
| `content_bottom_above_nav` | `88dp` | Bottom scroll padding above nav |

### 5.3 Radius Tokens

| Token | Value | Usage |
|---|---:|---|
| `radius_card_primary` | `24dp` | Main glass card |
| `radius_card_stat` | `20dp` | Stat card |
| `radius_card_list` | `20dp` | List card |
| `radius_image_list` | `16dp` | List image |
| `radius_button_primary` | `16dp` | Primary CTA |
| `radius_input` | `16dp` | Input field |
| `radius_chip` | `8dp` | Chip and small badge |
| `radius_nav_bar` | `36dp` | Nav bar pill |
| `radius_tooltip` | `12dp` | Tooltip |
| `radius_bottom_sheet` | `28dp` | Bottom sheet token |
| `radius_inner_card` | `16dp` | Nested inner card |
| `radius_inner_item` | `14dp` | Nested item |

### 5.4 Component Size Tokens

| Token | Value |
|---|---:|
| `header_height` | `72dp` |
| `header_padding_top` | `16dp` |
| `glass_icon_button_size` | `42dp` |
| `glass_icon_inner_size` | `20dp` |
| `nav_bar_height` | `72dp` |
| `nav_bar_margin_horizontal` | `24dp` |
| `nav_bar_margin_bottom` | `16dp` |
| `nav_icon_container_size` | `48dp` |
| `nav_icon_size` | `22dp` |
| `fab_size` | `56dp` |
| `fab_icon_size` | `28dp` |
| `fab_margin_right` | `20dp` |
| `fab_margin_bottom` | `96dp` |
| `card_min_height` | `120dp` |
| `stat_card_height` | `100dp` |
| `list_card_height` | `88dp` |
| `list_card_image_size` | `72dp` |
| `button_primary_height` | `52dp` |
| `button_secondary_height` | `48dp` |
| `input_height` | `56dp` |
| `chart_height` | `160dp` |
| `badge_container_size` | `72dp` |
| `badge_icon_size` | `32dp` |
| `weather_chip_height` | `32dp` |
| `segmented_control_height` | `44dp` |
| `avatar_size` | `42dp` |
| `notification_badge_size` | `8dp` |
| `location_icon_size` | `13dp` |

---

## 6. Glassmorphism System

Android XML cannot apply true backdrop blur to arbitrary drawables, so RainIQ simulates liquid glass using stacked layer-list drawables: translucent body, vertical specular highlight, and a bright rim stroke. Custom `GlassCardView` still exists for MaterialCard-based glass but many screens use drawable layer-lists directly.

### 6.1 Standard Liquid Glass Formula

Every implemented card-like glass surface uses this construction:

1. Body: semi-transparent dark green or white tint.
2. Highlight: top-to-bottom `angle="270"` gradient from white/specular to transparent.
3. Rim: `1dp` or `1.5dp` stroke using `glass_rim_outer` or semantic accent.
4. Radius: component token from `dimens.xml`.
5. Elevation: usually `0dp` for cards; floating nav uses layout elevation.

### 6.2 Main Glass Card

`@drawable/bg_glass_card`

| Layer | Exact spec |
|---|---|
| Body | `solid=@color/glass_primary`, radius `24dp` |
| Top specular | `angle=270`, `glass_specular_top` to `glass_specular_mid` to `#00FFFFFF`, `centerY=0.25` |
| Rim | `1dp`, `@color/glass_rim_outer`, radius `24dp` |

Use for primary content cards.

### 6.3 Stat And List Cards

| Drawable | Body | Radius | Specular | Rim |
|---|---|---:|---|---|
| `bg_glass_stat_card` | `glass_primary` | `20dp` | same as main card | `1dp glass_rim_outer` |
| `bg_glass_list_card` | `glass_primary` | `20dp` | same as main card | `1dp glass_rim_outer` |

### 6.4 Water Card

`@drawable/bg_glass_water_card`

| Layer | Exact spec |
|---|---|
| Body | `glass_primary`, radius `24dp` |
| Top specular | `glass_specular_top` to `glass_specular_mid` to transparent, `centerY=0.25` |
| Bottom reflection | `angle=90`, `#004ECDC4` to `#334ECDC4` |
| Rim | `1dp`, `#3D4ECDC4` |

Use for hero water metrics, rain tracking, total impact, and water-led cards.

### 6.5 AI Card

`@drawable/bg_glass_ai_card`

| Layer | Exact spec |
|---|---|
| Body | `glass_primary`, radius `24dp` |
| Teal top shimmer | `angle=270`, `#334ECDC4` to `#124ECDC4` to `#004ECDC4`, `centerY=0.5` |
| White specular | `glass_specular_mid` to transparent, `centerY=0.2` |
| Rim | `1dp`, `#3D4ECDC4` |

Use for Jal-Bot, AI tips, smart recommendations, and intelligent water insights.

### 6.6 Inner Card

`@drawable/bg_inner_card`

| Layer | Exact spec |
|---|---|
| Body | `inner_card_bg` (`#22FFFFFF`) |
| Specular | `angle=270`, `#30FFFFFF` to `#00FFFFFF`, `centerY=0.3` |
| Rim | `1dp`, `inner_card_border` |
| Radius | `16dp` |

Use only inside larger cards.

### 6.7 Chips And Small Containers

`@drawable/bg_glass_chip`

| Layer | Exact spec |
|---|---|
| Body | `glass_chip`, radius `8dp` |
| Specular | `glass_specular_mid` to transparent, `centerY=0.4` |
| Rim | `1dp`, `glass_rim_outer` |

Use for filter chips, icon backplates, small badges, and compact selectable surfaces.

### 6.8 Convex Glass Orbs

`@drawable/bg_glass_button`, `@drawable/bg_nav_circle_clone`, and `@drawable/bg_nav_add_glass` use an offset-shadow construction:

| Layer | Exact spec |
|---|---|
| Shadow base | Oval, `glass_rim_inner`, offset `top=2dp`, `left=2dp` |
| Body | Oval, `glass_button` or `glass_nav_circle`, offset `bottom=2dp`, `right=2dp` |
| Specular | Oval vertical white gradient, `centerY=0.35` to `0.4` |
| Rim | `1dp` or `1.5dp` stroke |

The center add orb adds a lime overlay `#1AC8E632` and a lime-white rim `#50C8E632`.

---

## 7. Header System

### 7.1 Home Header Layout

`@layout/layout_header`

| Property | Value |
|---|---|
| Root orientation | Horizontal |
| Root gravity | `center_vertical` |
| Padding start | `0dp` |
| Padding end | `20dp` |
| Padding top | `16dp` |
| Padding bottom | `16dp` |

### 7.2 Header Anatomy

| Element | Exact implementation |
|---|---|
| Logo | `84dp x 84dp`, margin start `2dp`, `@drawable/logo`, `fitCenter` |
| Brand block margin | `8dp` from logo |
| Brand text | Two `TextView`s: `Rain` white and `IQ` teal, both `39sp` SF Pro Bold |
| Tagline | Teal, `7sp`, all caps, letter spacing `0.06`, margin top `1dp` |
| Bell button | `42dp x 42dp`, `bg_glass_button`, icon `20dp` |
| Notification badge | `8dp x 8dp`, top/end margins `8dp`, orange dot |
| Avatar button | `42dp x 42dp`, margin start `10dp`, lime 2dp avatar ring |

---

## 8. Bottom Navigation

### 8.1 Main Shell Placement

`@layout/activity_main`

| Property | Value |
|---|---|
| Container width/height | `wrap_content` |
| Gravity | `bottom|center_horizontal` |
| Bottom margin | `32dp` |
| Background | `@drawable/bg_nav_pill_clone` |
| Elevation | `16dp` |
| Padding | `6dp` |
| Orientation | Horizontal |

### 8.2 Pill Background

`@drawable/bg_nav_pill_clone`

| Layer | Exact spec |
|---|---|
| Body | `glass_nav`, pill radius `100dp` |
| Specular | `glass_specular_mid` to transparent, `centerY=0.3` |
| Rim | `1dp glass_rim_outer`, radius `100dp` |

### 8.3 Nav Items

| Item | Container | Icon | Icon tint |
|---|---:|---|---|
| Dashboard | `56dp x 56dp`, margin end `4dp`, `bg_nav_circle_clone` | `ic_home`, `22dp` | `#E0E0E0` |
| Log Entry | `56dp x 56dp`, margin end `4dp`, `bg_nav_circle_clone` | `ic_plant`, `22dp` | `#E0E0E0` |
| Add | `56dp x 56dp`, margin end `4dp`, `bg_nav_add_glass`, elevation `4dp` | `ic_plus`, `22dp` | `#99CBFF7A` |
| AI Tips | `56dp x 56dp`, margin end `4dp`, `bg_nav_circle_clone` | `ic_target`, `22dp` | `#E0E0E0` |
| More | `56dp x 56dp`, `bg_nav_circle_clone` | `ic_grid`, `22dp` | `#E0E0E0` |

---

## 9. Buttons And Inputs

### 9.1 Primary CTA

`@drawable/bg_button_primary`

| Property | Value |
|---|---|
| Shape | Rectangle |
| Gradient | Linear, `angle=180`, `#C8E632` to `#4ECDC4` |
| Radius | `16dp` |
| Ripple | `accent_lime_dark` |
| Height token | `button_primary_height = 52dp` |
| Text style | `TextStyle.ButtonCta`, all caps, `14sp`, `letterSpacing=0.094` |

### 9.2 Secondary Button

`@drawable/bg_button_secondary`

| Property | Value |
|---|---|
| Fill | `glass_button` |
| Radius | `14dp` |
| Stroke | `1dp #33FFFFFF` |
| Height token | `button_secondary_height = 48dp` |

### 9.3 FAB

`@drawable/bg_fab`

| Property | Value |
|---|---|
| Shape | Oval |
| Fill | `accent_lime` |
| Size token | `56dp` |
| Icon token | `28dp` |

### 9.4 Inputs

| State | Drawable | Fill | Radius | Stroke |
|---|---|---|---:|---|
| Default | `bg_glass_input` | `input_bg` | `16dp` | `1dp input_border_default` |
| Focused primary | `bg_glass_input_focused` | `input_bg` | `16dp` | `1.5dp input_border_focus` |
| Focused AI/water | `bg_glass_input_focused_teal` | `input_bg` | `16dp` | `1.5dp input_border_focus_teal` |

Input height is `56dp`, hint color is `text_tertiary`, and typed text is `text_primary`.

---

## 10. Data Visualization

### 10.1 Progress Bar

`@drawable/bg_progress_bar`

| Part | Exact spec |
|---|---|
| Track | `inner_card_bg`, radius `4dp` |
| Progress | Clipped linear gradient, `angle=0`, `#4ECDC4` to `#C8E632`, radius `4dp` |

Teal-to-lime communicates water input progressing into achievement outcome.

### 10.2 Chart Fill Gradients

| Drawable | Gradient |
|---|---|
| `chart_fill_gradient` | `angle=270`, `#66C8E632` to `#1A4ECDC4` |
| `chart_fill_gradient_teal` | `angle=270`, `#664ECDC4` to `#004ECDC4` |

### 10.3 Semantic Chart Colors

| Meaning | Token |
|---|---|
| Grid | `chart_grid` |
| Axis labels | `chart_axis_text` |
| Primary data | `chart_primary` / lime |
| Secondary water data | `chart_secondary` / teal |
| Warning data | `chart_warning` / orange |

---

## 11. Status Indicators And Badges

| Component | Drawable | Exact spec |
|---|---|---|
| Avatar ring | `bg_avatar_ring` | Oval transparent fill, `2dp #80C8E632` stroke |
| Notification badge | `bg_notification_badge` | Oval `accent_orange`, `1.5dp bg_deepest` stroke |
| Active streak day | `bg_streak_day_active` | Oval `accent_lime` |
| Inactive streak day | `bg_streak_day_inactive` | Oval `inner_card_bg`, `1dp inner_card_border` |
| Active nav icon bg | `bg_nav_icon_active` | Oval `nav_icon_active_bg` |

---

## 12. Custom Animated Views

### 12.1 `GlassCardView`

Default MaterialCard glass implementation:

| Property | Value |
|---|---|
| Elevation | `0dp` |
| Max elevation | `0dp` |
| Background | `#9E0F1C0C` in class default |
| Stroke | `1dp #24FFFFFF` |
| Radius | `24dp` |
| Clickable/focusable | `false` |

`GlassLevel` variants:

| Level | Background | Radius |
|---|---:|---:|
| `BASE` | `#800F1C0C` | `20dp` |
| `CARD` | `#9E0F1C0C` | `24dp` |
| `FLOATING` | `#C70C180A` | `24dp` |
| `NAV` | `#CC0A140A` | `36dp` |
| `STAT` | `#9E0F1C0C` | `20dp` |

Note: `GlassCardView` predates the newer XML token values. Prefer XML drawables for current screens unless a MaterialCardView is required.

### 12.2 `WaterTankView`

| Property | Value |
|---|---|
| Tank stroke | `3dp`, `border_glass_light`, round caps |
| Water fill | `accent_teal`, alpha `210` |
| Wave amplitude | `8dp` |
| Wave animation | `2000ms`, infinite linear |
| Fill animation | `1500ms` |
| Tank shape | Rounded capsule, radius = half width |

### 12.3 `RainfallIllustrationView`

| Element | Value |
|---|---|
| Rain streaks | 28 streaks, teal `Color.argb(alpha, 78, 205, 196)` |
| Rain stroke | `1.5dp`, round cap |
| Rain angle | `0.26f` horizontal drift |
| Rain tick | `50ms`, infinite linear |
| House fill | `Color.argb(30,255,255,255)` |
| House outline | `2dp`, `Color.argb(90,255,255,255)` |
| Tank fill | Teal `Color.argb(180,78,205,196)` |
| Tank fill animation | `3000ms`, starts after `800ms`, target `65%` |

### 12.4 `WaterConversionView`

| Element | Value |
|---|---|
| Cloud fill | `Color.argb(60,255,255,255)` |
| Cloud stroke | `1.5dp`, `Color.argb(120,255,255,255)` |
| Arrow | `2dp`, teal `Color.argb(200,78,205,196)`, round cap |
| Raindrops | Teal, alpha around `0.4` to `0.9` |
| Tank fill | Teal `Color.argb(180,78,205,196)` |
| Tank shimmer | Lime `Color.argb(180,200,230,50)` |
| Cloud float | `2500ms`, reverse, max offset `6dp` |
| Arrow trace | `1200ms`, starts after `400ms` |
| Raindrop tick | `60ms`, infinite linear |
| Tank fill | `2500ms`, starts after `1600ms`, target `75%` |

### 12.5 `ImpactCounterView`

| Element | Value |
|---|---|
| Card fill | `Color.argb(80,15,28,12)` |
| Card stroke | `1dp`, `Color.argb(35,255,255,255)` |
| Card radius | `16dp` |
| Graph stroke | `2dp`, lime `Color.argb(160,200,230,50)` |
| Graph fill | Teal `Color.argb(30,78,205,196)` |
| Water value | `accent_teal` |
| Achievement/reward value | `accent_lime` |
| Label | `Color.argb(185,255,255,255)` |
| Counter animation | `1400ms`, decelerate `1.5f` |
| Badge animation | `450ms`, staggered `120ms`, overshoot `2.5f` |

---

## 13. Theme And System Bars

`Theme.RainIQ` extends `Theme.Material3.DayNight.NoActionBar`.

| Theme item | Value |
|---|---|
| `colorPrimary` | `accent_lime` |
| `colorOnPrimary` | `text_on_light_glass` |
| `colorSecondary` | `accent_teal` |
| `colorTertiary` | `accent_orange` |
| `colorError` | `accent_red_soft` |
| `colorSurface` | `bg_dark` |
| `colorSurfaceVariant` | `bg_forest` |
| `android:colorBackground` | `bg_deepest` |
| Status bar | Transparent |
| Navigation bar | `#00000000` |
| Cutout mode | `shortEdges` |

Shape appearances:

| Shape | Corner |
|---|---:|
| Small | `16dp` |
| Medium | `20dp` |
| Large | `24dp` |
| Bottom sheet | top corners `28dp`, bottom corners `0dp` |

Bottom sheet overlay dims the background by `0.4`.

---

## 14. Component Usage Rules

Use `bg_glass_card` for ordinary content, `bg_glass_water_card` for water-led hero cards, `bg_glass_ai_card` for AI/Jal-Bot surfaces, `bg_inner_card` for nested panels, and `bg_glass_chip` for small badges or icon holders.

Use teal for water quantities, rain, AI, flow, clouds, and smart states. Use lime for CTAs, saved money, goals, streaks, positive impact, active achievement states, and success. Use orange for alerts and unread/attention badges. Use red only for destructive or critical states.

Keep horizontal screen margins at `20dp`, main card radius at `24dp`, stat/list card radius at `20dp`, input radius at `16dp`, and chip radius at `8dp`. Do not introduce flat opaque cards unless the screen intentionally leaves the Liquid Nature Glass language.

---

## 15. Pixel-Match Checklist

Before shipping a new or modified screen:

1. Root background must include the photo/gradient, black overlay, and green tint overlay.
2. Primary glass cards must use the 3-layer body/specular/rim construction.
3. Water cards must include teal bottom reflection and teal rim.
4. AI cards must include teal top shimmer and teal rim.
5. Header logo must remain `84dp`, brand text `39sp`, actions `42dp`.
6. Bottom nav must remain a floating pill with `32dp` bottom margin, `6dp` padding, `56dp` orbs, and `22dp` icons.
7. Inputs must use `56dp` height and `16dp` radius.
8. Primary CTAs must use the lime-to-teal gradient and `52dp` height.
9. Typography should prefer SF Pro Display; Inter usage should be deliberate legacy/fallback only.
10. Cards should avoid Material shadow elevation unless they are floating navigation or CTA-like objects.
