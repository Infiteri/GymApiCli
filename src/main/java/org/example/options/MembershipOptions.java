package org.example.options;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.example.entities.Membership;
import org.example.entities.MembershipType;
import org.example.entities.User;
import org.example.utils.ApiUtils;

import java.net.HttpURLConnection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class MembershipOptions
{

        public static Membership MembershipFromJson(JsonObject jsonObject)
        {

                Membership mem = new Membership();

                if (jsonObject.has("id") && !jsonObject.get("id").isJsonNull())
                        mem.SetId(jsonObject.get("id").getAsInt());

                if (jsonObject.has("state") && !jsonObject.get("state").isJsonNull())
                        mem.SetState(jsonObject.get("state").getAsString());

                if (jsonObject.has("lastPayed") && !jsonObject.get("lastPayed").isJsonNull())
                        mem.SetLastPayed(LocalDateTime.parse(jsonObject.get("lastPayed").getAsString()));

                if (jsonObject.has("createdAt") && !jsonObject.get("createdAt").isJsonNull())
                        mem.SetCreatedAt(LocalDateTime.parse(jsonObject.get("createdAt").getAsString()));

                if (jsonObject.has("type") && jsonObject.get("type").isJsonObject())
                {
                        JsonObject typeObj = jsonObject.getAsJsonObject("type");
                        MembershipType type = new MembershipType();

                        if (typeObj.has("name") && !typeObj.get("name").isJsonNull())
                                type.SetName(typeObj.get("name").getAsString());

                        if (typeObj.has("startTime") && !typeObj.get("startTime").isJsonNull())
                                type.SetStartTime(LocalTime.parse(typeObj.get("startTime").getAsString()));

                        if (typeObj.has("endTime") && !typeObj.get("endTime").isJsonNull())
                                type.SetEndTime(LocalTime.parse(typeObj.get("endTime").getAsString()));

                        mem.SetType(type);
                }

                if (jsonObject.has("user") && jsonObject.get("user").isJsonObject())
                {
                        JsonObject userObj = jsonObject.getAsJsonObject("user");
                        User user = new User();

                        if (userObj.has("name") && !userObj.get("name").isJsonNull())
                                user.SetName(userObj.get("name").getAsString());

                        if (userObj.has("birthday") && !userObj.get("birthday").isJsonNull())
                                user.SetBirthday(LocalDate.parse(userObj.get("birthday").getAsString()));

                        if (userObj.has("phoneNumber") && !userObj.get("phoneNumber").isJsonNull())
                                user.SetPhoneNumber(userObj.get("phoneNumber").getAsString());

                        mem.SetUser(user);
                }

                return mem;
        }


        public static void DisplayMembership(Membership membership)
        {
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");

                System.out.println("----- MEMBERSHIP INFO -----");
                System.out.println("ID         : " + membership.GetId());
                System.out.println("State      : " + membership.GetState());

                if (membership.GetType() != null)
                {
                        System.out.println("Type       : " + membership.GetType().GetName());
                } else
                {
                        System.out.println("Type       : N/A");
                }

                if (membership.GetLastPayed() != null)
                {
                        System.out.println("Last Payed : " + membership.GetLastPayed().format(dateFormatter));
                } else
                {
                        System.out.println("Last Payed : N/A");
                }

                if (membership.GetCreatedAt() != null)
                {
                        System.out.println("Created At : " + membership.GetCreatedAt().format(dateFormatter));
                } else
                {
                        System.out.println("Created At : N/A");
                }

                if (membership.GetUser() != null)
                {
                        System.out.println("User       : " + membership.GetUser().GetName());
                } else
                {
                        System.out.println("User       : N/A");
                }

                System.out.println("---------------------------");
        }


        public static Membership GetMembershipWithPhoneNumber(String phoneNumber)
        {
                HttpURLConnection conn = null;
                try
                {
                        conn = ApiUtils.FetchApi(
                                "/api/v1/membership/phoneNumber?phoneNumber=" + phoneNumber,
                                ApiUtils.RequestMethod.GET, null
                        );

                        if (conn.getResponseCode() != 200)
                        {
                                return null;
                        }

                        String result = ApiUtils.ReadApiResponse(conn);
                        Membership mem = MembershipFromJson(JsonParser.parseString(result).getAsJsonObject());
                        if (mem != null)
                        {
                                return mem;
                        } else
                        {
                                return null;
                        }
                } catch (Exception e)
                {
                        e.printStackTrace();
                } finally
                {
                        if (conn != null) conn.disconnect();
                }

                return null;
        }


        private static JsonObject GetTypeFromName(String typeName)
        {
                HttpURLConnection conn = null;
                try
                {
                        conn = ApiUtils.FetchApi(
                                "/api/v1/membership_type/name?name=" + typeName,
                                ApiUtils.RequestMethod.GET, null
                        );

                        if (conn.getResponseCode() != 200)
                        {
                                return null;
                        }

                        String result = ApiUtils.ReadApiResponse(conn);
                        return JsonParser.parseString(result).getAsJsonObject();
                } catch (Exception e)
                {
                        e.printStackTrace();
                } finally
                {
                        if (conn != null) conn.disconnect();
                }

                return null;
        }

        public static boolean CreateMembership(String userPhone, String memType)
        {
                JsonObject obj = new JsonObject();

                // nested "user"
                JsonObject user = UserOptions.FindUserWithPhoneNumberJson(userPhone);
                if (user == null) return false;
                obj.add("user", user);

                // nested "type"
                var type = GetTypeFromName(memType);
                obj.add("type", type);

                // state
                obj.addProperty("state", "NOT_IN_GYM");
                HttpURLConnection conn = null;
                try
                {
                        conn = ApiUtils.FetchApi(
                                "/api/v1/membership/create",
                                ApiUtils.RequestMethod.POST, obj
                        );

                        if (conn.getResponseCode() != 200)
                        {
                                return false;
                        }

                        return true;
                } catch (Exception e)
                {
                        e.printStackTrace();
                } finally
                {
                        if (conn != null) conn.disconnect();
                }

                return false;
        }

        public static boolean RemoveMembership(String userPhone)
        {
                Membership membership = GetMembershipWithPhoneNumber(userPhone);
                if (membership == null) return false;
                HttpURLConnection conn = null;
                try
                {
                        conn = ApiUtils.FetchApi(
                                "/api/v1/membership?id=" + membership.GetId(),
                                ApiUtils.RequestMethod.DELETE, null
                        );

                        if (conn.getResponseCode() != 200)
                        {
                                return false;
                        }

                        return true;
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
