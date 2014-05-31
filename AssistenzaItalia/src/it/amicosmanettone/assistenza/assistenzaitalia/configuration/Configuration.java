package it.amicosmanettone.assistenza.assistenzaitalia.configuration;

public class Configuration{
	
	public static String apiURL = "http://www.amicosmanettone.it/api/v2/index.php";
	public static String apiTECNICI = "http://www.amicosmanettone.it/api/v2/tecnici.php";
	public static String apiGCM = "http://www.amicosmanettone.it/api/v2/registraDevice.php";
	public static String avatarURL = "http://www.amicosmanettone.it/images/comprofiler/";

	public static String login_tag = "login";
	public static String recoveryUserImage_tag = "recoveryUserImage";
	public static String checkDiagnosisVersion_tag = "checkDiagnosisVersion";
	public static String incrementRichieste_tag = "incrementRichieste";
	
	public static String registerDevice_tag = "registerDevice";
	public static String updateUserDevice_tag = "updateUserDevice";
	public static String disableChatNotification_tag = "disableChatNotification";
	public static String enableChatNotification_tag = "enableChatNotification";
	
	public static String sendChatNotification_tag = "sendChatNotification";
	public static String deleteMessage_tag = "deleteMessage";
	public static String checkVersion_tag = "checkVersion";
	public static String getUserKarma_tag = "getUserKarma";
	
	public static String FACEBOOK_URL = "https://www.facebook.com/pages/AmicoSmanettoneit/343874855644946";
	public static String TWITTER_URL = "https://twitter.com/AmicoSmanettone";
	public static String GOOGLE_URL = "https://plus.google.com/116985865329983226764/posts";
	public static String AMICOSMANETTONE_URL = "http://www.amicosmanettone.it";
	public static String PLAYSTORE_URL = "https://play.google.com/store/apps/details?id=it.amicosmanettone.assistenza.assistenzaitalia";
	
	public static String post_tag = "POST";
	public static String get_tag = "GET";
	
	public static int delay = 100;

	public static String CHAT_URL="http://www.amicosmanettone.it/index.php?option=com_kide&no_html=1&tmpl=component&task=reload";
	
	public static String CHAT_URL_SEND="http://www.amicosmanettone.it/index.php?option=com_kide&no_html=1&tmpl=component&task=add";
	
	public static String CHAT_URL_SENDUSERONLINE = "http://www.amicosmanettone.it/index.php?option=com_kide&no_html=1&tmpl=component&task=sesiones&show_sessions=1";

	public static String FORM_URL="http://www.amicosmanettone.it/index.php?option=com_rsform&formId=78";
	
	
	/*** GCM ***/
    public static final String SENDER_ID = "14806801099";
	
}//End class...