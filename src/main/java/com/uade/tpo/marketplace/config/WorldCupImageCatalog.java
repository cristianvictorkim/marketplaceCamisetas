package com.uade.tpo.marketplace.config;

final class WorldCupImageCatalog {

    private static final String ARGENTINA_HOME_IMAGE = "https://cdn.shopify.com/s/files/1/0591/0478/8538/files/image_53104866-d11b-446b-842f-9e37e19d1c38.png?v=1774372574";
    private static final String ARGENTINA_AWAY_IMAGE = "https://cdn.shopify.com/s/files/1/0591/0478/8538/files/image_b6a5a8ac-8a79-460f-904c-9fc586a854f0.jpg?v=1775563942";

    private WorldCupImageCatalog() {
    }

    static String imageFor(String country, String version) {
        String key = country + "|" + version;

        switch (key) {
            case "Canada|Titular":
                return "https://static.nike.com/a/images/t_web_pw_592_v2/f_auto/u_9ddf04c7-2a9a-4d76-add1-d15af8f0263d,c_scale,fl_relative,w_1.0,h_1.0,fl_layer_apply/92c01fd2-e837-4170-b864-df6111bb9847/WC+CAN+UNISEX+MATCH+HOME+JERSE.png";
            case "Canada|Alternativa":
                return "https://static.nike.com/a/images/t_web_pw_592_v2/f_auto/u_9ddf04c7-2a9a-4d76-add1-d15af8f0263d,c_scale,fl_relative,w_1.0,h_1.0,fl_layer_apply/fff8218d-3890-4d9e-b87d-f957e84d1f9a/WC+CAN+M+STADIUM+AWAY+JERSEY.png";
            case "Mexico|Titular":
                return "https://cdn.shopify.com/s/files/1/0591/0478/8538/files/image_bb8e2e3e-8c87-4469-b928-0dffe04d9d6f.png?v=1774372426";
            case "Mexico|Alternativa":
                return "https://cdn.shopify.com/s/files/1/0591/0478/8538/files/image_8c109ac7-6e21-4d48-b300-b77698d533cd.jpg?v=1775564098";
            case "Estados Unidos|Titular":
                return "https://static.nike.com/a/images/t_web_pw_592_v2/f_auto/u_9ddf04c7-2a9a-4d76-add1-d15af8f0263d,c_scale,fl_relative,w_1.0,h_1.0,fl_layer_apply/6f9998cf-5845-4fb7-8931-e56f44e29344/USA+M+NK+DF+JSY+SS+STAD+HM.png";
            case "Estados Unidos|Alternativa":
                return "https://static.nike.com/a/images/t_web_pw_592_v2/f_auto/u_9ddf04c7-2a9a-4d76-add1-d15af8f0263d,c_scale,fl_relative,w_1.0,h_1.0,fl_layer_apply/22385879-dbc2-4057-af35-b4fa5f0db41c/USA+M+NK+DFADV+JSY+SS+MTCH+AW.png";
            case "Australia|Titular":
                return "https://static.nike.com/a/images/t_web_pw_592_v2/f_auto/u_9ddf04c7-2a9a-4d76-add1-d15af8f0263d,c_scale,fl_relative,w_1.0,h_1.0,fl_layer_apply/966eefd4-ab0f-48bf-be71-0404bb951ddc/AUS+M+NK+DF+JSY+SS+STAD+HM.png";
            case "Australia|Alternativa":
                return "https://static.nike.com/a/images/t_web_pw_592_v2/f_auto/u_9ddf04c7-2a9a-4d76-add1-d15af8f0263d,c_scale,fl_relative,w_1.0,h_1.0,fl_layer_apply/fe4906a3-0fb7-422e-971e-3be08d3cc942/AUS+M+NK+DF+JSY+SS+STAD+AW.png";
            case "Japon|Titular":
                return "https://cdn.shopify.com/s/files/1/0591/0478/8538/files/JN1867_1_APPAREL_Photography_FrontCenterView_white.jpg?v=1774372437";
            case "Japon|Alternativa":
                return "https://cdn.shopify.com/s/files/1/0591/0478/8538/files/image_ce443127-e9d3-4c92-86b4-a41c60703084.jpg?v=1775564097";
            case "Corea del Sur|Titular":
                return "https://static.nike.com/a/images/t_web_pw_592_v2/f_auto/u_9ddf04c7-2a9a-4d76-add1-d15af8f0263d,c_scale,fl_relative,w_1.0,h_1.0,fl_layer_apply/0ea59d55-797c-4eb1-a5dc-67c9962b7dd3/KOR+M+NK+DF+JSY+SS+STAD+HM.png";
            case "Corea del Sur|Alternativa":
                return "https://static.nike.com/a/images/t_web_pw_592_v2/f_auto/u_9ddf04c7-2a9a-4d76-add1-d15af8f0263d,c_scale,fl_relative,w_1.0,h_1.0,fl_layer_apply/9da00dd2-a374-4c2f-ae0a-64b9da8ecf62/KOR+M+NK+DF+JSY+SS+STAD+AW.png";
            case "Qatar|Alternativa":
                return "https://cdn.shopify.com/s/files/1/0591/0478/8538/files/KF0824_1_APPAREL_Photography_FrontCenterView_white.jpg?v=1778514535";
            case "Cabo Verde|Titular":
                return "https://cdn.shopify.com/s/files/1/0591/0478/8538/files/1_ac6de1af-2202-413e-83b4-3c84537ff44c.png?v=1781102784";
            case "Cabo Verde|Alternativa":
                return "https://cdn.shopify.com/s/files/1/0591/0478/8538/files/6.png?v=1781103467";
            case "Costa de Marfil|Titular":
                return "https://images.puma.com/image/upload/f_auto,q_auto,b_rgb:fafafa,w_2000,h_2000/global/783182/01/fnd/EEA/fmt/png/Cote-Divoire-2026-Home-Jersey-Men";
            case "Costa de Marfil|Alternativa":
                return "https://images.puma.com/image/upload/f_auto,q_auto,b_rgb:fafafa,w_2000,h_2000/global/783188/01/fnd/EEA/fmt/png/Cote-Divoire-2026-Away-Jersey-Men";
            case "Egipto|Titular":
                return "https://cdn.shopify.com/s/files/1/0591/0478/8538/files/image_67d30dc8-9ac9-4374-8d78-331f9bab4ee3.jpg?v=1775563963";
            case "Egipto|Alternativa":
                return "https://images.puma.com/image/upload/f_auto,q_auto,b_rgb:fafafa,w_2000,h_2000/global/783557/01/fnd/EEA/fmt/png/Egypt-2026-Away-Jersey-Men";
            case "Ghana|Titular":
                return "https://images.puma.com/image/upload/f_auto,q_auto,b_rgb:fafafa,w_2000,h_2000/global/783419/01/fnd/EEA/fmt/png/Ghana-2026-Home-Jersey-Men";
            case "Ghana|Alternativa":
                return "https://images.puma.com/image/upload/f_auto,q_auto,b_rgb:fafafa,w_2000,h_2000/global/783425/01/fnd/EEA/fmt/png/Ghana-2026-Away-Jersey-Men";
            case "Marruecos|Titular":
                return "https://cdn.shopify.com/s/files/1/0591/0478/8538/files/image_493a4877-2abe-48e6-bbcd-5971689ba863.jpg?v=1775564037";
            case "Marruecos|Alternativa":
                return "https://images.puma.com/image/upload/f_auto,q_auto,b_rgb:fafafa,w_2000,h_2000/global/783318/01/fnd/EEA/fmt/png/Morocco-2026-Away-Jersey-Men";
            case "Senegal|Titular":
                return "https://cdn.shopify.com/s/files/1/0591/0478/8538/files/image_12e3a4e0-5f8f-44c7-9e96-17f2ab2e78de.jpg?v=1775564023";
            case "Senegal|Alternativa":
                return "https://images.puma.com/image/upload/f_auto,q_auto,b_rgb:fafafa,w_2000,h_2000/global/783171/01/fnd/EEA/fmt/png/Senegal-2026-Away-Jersey-Men";
            case "Sudafrica|Titular":
                return "https://cdn.shopify.com/s/files/1/0591/0478/8538/files/KY2209_1_APPAREL_Photography_FrontCenterView_white.jpg?v=1780669086";
            case "Haiti|Titular":
                return "https://cdn.shopify.com/s/files/1/0591/0478/8538/files/FIFAMZ0441-1.png?v=1781686329";
            case "Haiti|Alternativa":
                return "https://cdn.shopify.com/s/files/1/0591/0478/8538/files/FIFAMZ0442-1.png?v=1781686439";
            case "Argentina|Titular":
                return ARGENTINA_HOME_IMAGE;
            case "Argentina|Alternativa":
                return ARGENTINA_AWAY_IMAGE;
            case "Brasil|Titular":
                return "https://static.nike.com/a/images/t_web_pw_592_v2/f_auto/u_9ddf04c7-2a9a-4d76-add1-d15af8f0263d,c_scale,fl_relative,w_1.0,h_1.0,fl_layer_apply/244bbcfc-6266-43d5-8322-3c78f63d9528/CBF+Y+NK+DFADV+JSY+SS+MATCH+HM.png";
            case "Brasil|Alternativa":
                return "https://cdn.shopify.com/s/files/1/0591/0478/8538/files/image_5a01cfb2-988b-42a3-95a4-39da4fb8791d.jpg?v=1775563937";
            case "Colombia|Titular":
                return "https://cdn.shopify.com/s/files/1/0591/0478/8538/files/image_695e8c00-a951-4ed8-af81-899e39ed5503.png?v=1774372495";
            case "Colombia|Alternativa":
                return "https://cdn.shopify.com/s/files/1/0591/0478/8538/files/image_1dc4aa41-f0ab-40ff-9187-e3deeb343848.jpg?v=1775563987";
            case "Paraguay|Titular":
                return "https://cdn.shopify.com/s/files/1/0591/0478/8538/files/FIFAMZ0420-1.png?v=1778684987";
            case "Paraguay|Alternativa":
                return "https://images.puma.com/image/upload/f_auto,q_auto,b_rgb:fafafa,w_2000,h_2000/global/783304/01/fnd/EEA/fmt/png/Paraguay-2026-Authentic-Away-Jersey-Men";
            case "Nueva Zelanda|Titular":
                return "https://images.puma.com/image/upload/f_auto,q_auto,b_rgb:fafafa,w_2000,h_2000/global/783262/01/fnd/EEA/fmt/png/New-Zealand-2026-Home-Jersey-Men";
            case "Nueva Zelanda|Alternativa":
                return "https://images.puma.com/image/upload/f_auto,q_auto,b_rgb:fafafa,w_2000,h_2000/global/783267/01/fnd/EEA/fmt/png/New-Zealand-2026-Away-Jersey-Men";
            case "Austria|Titular":
                return "https://images.puma.com/image/upload/f_auto,q_auto,b_rgb:fafafa,w_2000,h_2000/global/783200/01/fnd/EEA/fmt/png/Austria-2026-Home-Jersey-Men";
            case "Austria|Alternativa":
                return "https://images.puma.com/image/upload/f_auto,q_auto,b_rgb:fafafa,w_2000,h_2000/global/783207/01/fnd/EEA/fmt/png/Austria-2026-Away-Jersey-Men";
            case "Belgica|Titular":
                return "https://cdn.shopify.com/s/files/1/0591/0478/8538/files/image_dde2dcc5-5067-4cf8-b2c9-05bc9461709a.png?v=1774372515";
            case "Belgica|Alternativa":
                return "https://cdn.shopify.com/s/files/1/0591/0478/8538/files/image_d0ac9a7d-47d3-4a90-ac01-6f7940754fb0.jpg?v=1775564063";
            case "Croacia|Titular":
                return "https://static.nike.com/a/images/t_web_pw_592_v2/f_auto/u_9ddf04c7-2a9a-4d76-add1-d15af8f0263d,c_scale,fl_relative,w_1.0,h_1.0,fl_layer_apply/8c4082c4-2ac4-4199-9551-b33a9d182668/SP26+WC+CROATIA+SS+JSY+STAD+HM.png";
            case "Croacia|Alternativa":
                return "https://static.nike.com/a/images/t_web_pw_592_v2/f_auto/u_9ddf04c7-2a9a-4d76-add1-d15af8f0263d,c_scale,fl_relative,w_1.0,h_1.0,fl_layer_apply/0440b925-f2a3-48b2-9ca1-dc9a020914c8/SP26+WC+CROATIA+SS+JSY+STAD+AW.png";
            case "Chequia|Titular":
                return "https://images.puma.com/image/upload/f_auto,q_auto,b_rgb:fafafa,w_2000,h_2000/global/783252/01/fnd/EEA/fmt/png/Czechia-2026-Home-Jersey-Men";
            case "Chequia|Alternativa":
                return "https://images.puma.com/image/upload/f_auto,q_auto,b_rgb:fafafa,w_2000,h_2000/global/783257/01/fnd/EEA/fmt/png/Czechia-2026-Away-Jersey-Men";
            case "Inglaterra|Titular":
                return "https://cdn.shopify.com/s/files/1/0591/0478/8538/files/image_53693450-69ae-4939-b97e-4dea991953c1.jpg?v=1775563972";
            case "Inglaterra|Alternativa":
                return "https://cdn.shopify.com/s/files/1/0591/0478/8538/files/image_80255efb-0ad1-4b46-aafd-9ed0fa63706d.jpg?v=1775564089";
            case "Francia|Titular":
                return "https://cdn.shopify.com/s/files/1/0591/0478/8538/files/image_97c7b304-c8ac-48bb-b2a2-d6349ac9c33a.jpg?v=1775564062";
            case "Francia|Alternativa":
                return "https://cdn.shopify.com/s/files/1/0591/0478/8538/files/image_f7f029ac-d57c-4aab-ab4e-45d5d056ac29.jpg?v=1775564083";
            case "Alemania|Titular":
                return "https://cdn.shopify.com/s/files/1/0591/0478/8538/files/KD8363_1_APPAREL_Photography_FrontCenterView_white.jpg?v=1774372475";
            case "Alemania|Alternativa":
                return "https://cdn.shopify.com/s/files/1/0591/0478/8538/files/image_9b42b582-6a6d-41a8-bd3a-e6fc6caeb107.jpg?v=1775564043";
            case "Paises Bajos|Titular":
                return "https://cdn.shopify.com/s/files/1/0591/0478/8538/files/image_d5c18bb6-8ee2-4e49-970a-1a97dc417fbe.jpg?v=1775563996";
            case "Paises Bajos|Alternativa":
                return "https://cdn.shopify.com/s/files/1/0591/0478/8538/files/image_3cc6ac0e-59b4-4139-8cdd-cb61c9ab5a2c.jpg?v=1775563980";
            case "Noruega|Titular":
                return "https://static.nike.com/a/images/t_web_pw_592_v2/f_auto/u_9ddf04c7-2a9a-4d76-add1-d15af8f0263d,c_scale,fl_relative,w_1.0,h_1.0,fl_layer_apply/2b5a5b96-024a-4268-98a2-ac6d3e584401/NOR+Y+NK+DFADV+JSY+SS+MATCH+HM.png";
            case "Noruega|Alternativa":
                return "https://static.nike.com/a/images/t_web_pw_592_v2/f_auto/u_9ddf04c7-2a9a-4d76-add1-d15af8f0263d,c_scale,fl_relative,w_1.0,h_1.0,fl_layer_apply/f9627fde-9594-4bc0-8555-78707e8dd0ac/NOR+M+NK+DF+JSY+SS+STAD+AW.png";
            case "Portugal|Titular":
                return "https://cdn.shopify.com/s/files/1/0591/0478/8538/files/image_fe0c6316-72b0-4482-8838-c7f6bbd7fba6.jpg?v=1774372661";
            case "Portugal|Alternativa":
                return "https://cdn.shopify.com/s/files/1/0591/0478/8538/files/FIFAMZ0368-1.png?v=1778690010";
            case "Escocia|Titular":
                return "https://cdn.shopify.com/s/files/1/0591/0478/8538/files/image_d6d1326d-6d5c-44e0-bae6-70c9df6db32e.jpg?v=1775564071";
            case "Espana|Titular":
                return "https://cdn.shopify.com/s/files/1/0591/0478/8538/files/image_8b6f5358-fe86-4ed5-96dc-051e62e3e943.png?v=1774372373";
            case "Espana|Alternativa":
                return "https://cdn.shopify.com/s/files/1/0591/0478/8538/files/image_5b3327eb-0447-42d7-9b83-1a5943051fe8.jpg?v=1775563985";
            case "Suecia|Titular":
                return "https://cdn.shopify.com/s/files/1/0591/0478/8538/files/KC3044_1_APPAREL_Photography_FrontCenterView_white.jpg?v=1774372357";
            case "Suiza|Titular":
                return "https://images.puma.com/image/upload/f_auto,q_auto,b_rgb:fafafa,w_2000,h_2000/global/783218/01/fnd/EEA/fmt/png/Switzerland-2026-Home-Jersey-Men";
            case "Suiza|Alternativa":
                return "https://images.puma.com/image/upload/f_auto,q_auto,b_rgb:fafafa,w_2000,h_2000/global/783225/01/fnd/EEA/fmt/png/Switzerland-2026-Away-Jersey-Men";
            case "Turquia|Titular":
                return "https://static.nike.com/a/images/t_web_pw_592_v2/f_auto/u_9ddf04c7-2a9a-4d76-add1-d15af8f0263d,c_scale,fl_relative,w_1.0,h_1.0,fl_layer_apply/f299ff88-11c7-47fc-b6e9-db58172481cf/SP26+WC+TURKEY+SS+JSY+STAD+HM.png";
            case "Turquia|Alternativa":
                return "https://static.nike.com/a/images/t_web_pw_592_v2/f_auto/u_9ddf04c7-2a9a-4d76-add1-d15af8f0263d,c_scale,fl_relative,w_1.0,h_1.0,fl_layer_apply/61b3bb81-f088-4bb5-ac1a-241fbce89a8b/SP26+WC+TURKEY+SS+JSY+STAD+AW.png";
            default:
                return null;
        }
    }
}
