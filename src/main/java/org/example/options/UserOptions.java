package org.example.options;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.example.entities.User;
import org.example.utils.ApiUtils;

import java.net.HttpURLConnection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UserOptions
{
        public static User UserFromJson(JsonObject jsonObject)
        {
                LocalDate birthday = LocalDate.parse(jsonObject.get("birthday").getAsString());
                String name = jsonObject.get("name").getAsString();
                String phone = jsonObject.get("phoneNumber").getAsString();

                User user = new User();
                user.SetName(name);
                user.SetBirthday(birthday);
                user.SetPhoneNumber(phone);
                return user;
        }

        public static JsonObject UserToJson(User user)
        {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("id", user.GetId());
                jsonObject.addProperty("name", user.GetName());
                jsonObject.addProperty("birthday", user.GetBirthday().toString()); // LocalDate -> "yyyy-MM-dd"
                jsonObject.addProperty("phoneNumber", user.GetPhoneNumber());
                return jsonObject;
        }


        public static void DisplayUser(User user)
        {
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");

                System.out.println("-------- USER INFO --------");
                System.out.println("Name       : " + user.GetName());

                if (user.GetBirthday() != null)
                {
                        System.out.println("Birthday   : " + user.GetBirthday().format(dateFormatter));
                } else
                {
                        System.out.println("Birthday   : N/A");
                }

                System.out.println("Phone      : " + user.GetPhoneNumber());
                System.out.println("---------------------------");
        }

        public static void AddUser(User user)
        {
                HttpURLConnection conn = null;
                try
                {
                        JsonObject obj = new JsonObject();
                        obj.addProperty("name", user.GetName());
                        obj.addProperty("birthday", user.GetBirthday().toString());
                        obj.addProperty("phoneNumber", user.GetPhoneNumber());

                        conn = ApiUtils.FetchApi("/api/v1/user/create", ApiUtils.RequestMethod.POST, obj);

                        if (conn.getResponseCode() != 200)
                        {
                                System.out.println("Error(CreateUser): " + conn.getResponseCode() + "");
                                return;
                        }

                } catch (Exception e)
                {
                        e.printStackTrace();
                } finally
                {
                        if (conn != null) conn.disconnect();
                }
        }

        public static void DisplayUsers()
        {
                HttpURLConnection conn = null;
                try
                {
                        conn = ApiUtils.FetchApi("/api/v1/user/all", ApiUtils.RequestMethod.GET, null);

                        if (conn.getResponseCode() != 200)
                        {
                                System.out.println("Error(GetAllUsers): " + conn.getResponseCode());
                        }

                        String result = ApiUtils.ReadApiResponse(conn);
                        JsonArray resultJsonArray = new JsonParser().parse(result).getAsJsonArray();

                        for (JsonElement jsonElement : resultJsonArray)
                        {
                                User user = UserFromJson(jsonElement.getAsJsonObject());
                                DisplayUser(user);
                        }

                } catch (Exception e)
                {
                        e.printStackTrace();
                } finally
                {
                        if (conn != null) conn.disconnect();
                }
        }

        public static JsonObject FindUserWithPhoneNumberJson(String phoneNumber)
        {
                HttpURLConnection conn = null;
                try
                {
                        conn = ApiUtils.FetchApi("/api/v1/user/phoneNumber?phoneNumber=" + phoneNumber, ApiUtils.RequestMethod.GET, null);

                        if (conn.getResponseCode() != 200)
                        {
                                return null;
                        }

                        String result = ApiUtils.ReadApiResponse(conn);
                        JsonObject obj = new JsonParser().parse(result).getAsJsonObject();
                        return obj;
                } catch (Exception e)
                {
                        e.printStackTrace();
                } finally
                {
                        if (conn != null) conn.disconnect();
                }
                return null;
        }

        public static User FindUserWithPhoneNumber(String phoneNumber)
        {
                JsonObject obj = FindUserWithPhoneNumberJson(phoneNumber);
                if(obj == null) return null;
                return UserFromJson(obj);
        }

        public static boolean RemoveUser(String phoneNumber)
        {

                HttpURLConnection conn = null;
                try
                {
                        conn = ApiUtils.FetchApi("/api/v1/user/phoneNumber?phoneNumber=" + phoneNumber, ApiUtils.RequestMethod.DELETE, null);

                        if (conn.getResponseCode() != 200) return false;
                        else return true;

                } catch (Exception e)
                {
                        e.printStackTrace();
                } finally
                {
                        if (conn != null) conn.disconnect();
                }

                return false;
        }
}
