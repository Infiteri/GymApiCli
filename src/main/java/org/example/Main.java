package org.example;

import org.example.entities.Membership;
import org.example.entities.User;
import org.example.options.MembershipOptions;
import org.example.options.UserOptions;
import org.example.utils.ApiUtils;
import com.google.gson.*;
import org.example.utils.Utils;

import java.net.HttpURLConnection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main
{
        // note that main is the whole menu, everything else will be in options, so the menu reads the data and sends the data to "options.___Option"
        static Scanner scanner = new Scanner(System.in);

        public static void ShowHelp()
        {
                System.out.println("Help menu: ");
                System.out.println("display_users - will display all users");
                System.out.println("find_user - will find a specific user after phone number");
                System.out.println("add_user - will add a new user with data given");
                System.out.println("remove_user - will remove an user from phone number ");
                System.out.println("find_membership - will find a membership from an user phone number ");
                System.out.println("add_membership - will add a membership to an user phone number ");
                System.out.println("remove_membership - will remove a membership with a phone number ");
        }


        public static void FindUser()
        {
                System.out.print("Enter phone Number: ");
                String phoneNumber = scanner.nextLine();

                {

                        if (Utils.ValidatePhoneNumber(phoneNumber))
                        {
                                phoneNumber = Utils.FormatPhoneNumberForQuery(phoneNumber);
                        } else
                        {
                                System.out.println("Invalid phone number");
                                return;
                        }
                }

                User user = UserOptions.FindUserWithPhoneNumber(phoneNumber);
                if (user != null) UserOptions.DisplayUser(user);
                else System.out.println("No user found with phone number " + phoneNumber);
        }

        public static void AddUser()
        {
                String name, phoneNumber, birthday;
                System.out.print("Enter Name: ");
                name = scanner.nextLine();
                System.out.print("Enter Phone Number: ");
                phoneNumber = scanner.nextLine();
                System.out.print("Enter Birthday: ");
                birthday = scanner.nextLine();

                // check format of phone number
                if (!Utils.ValidatePhoneNumber(phoneNumber))
                {
                        System.out.println("Invalid phone number format. It must start with + and contain only digits.");
                        return;
                }


                User user = new User();
                user.SetName(name);
                user.SetPhoneNumber(phoneNumber);
                user.SetBirthday(LocalDate.parse(birthday));
                UserOptions.AddUser(user);
        }

        public static void RemoveUser()
        {
                System.out.print("Enter phone Number: ");
                String phoneNumber = scanner.nextLine();

                if (!Utils.ValidatePhoneNumber(phoneNumber))
                {
                        System.out.println("Invalid phone number format. It must start with +");
                        return;
                } else phoneNumber = Utils.FormatPhoneNumberForQuery(phoneNumber);

                if (!UserOptions.RemoveUser(phoneNumber))
                {
                        System.out.println("Couldn't remove user");
                } else
                {
                        System.out.println("Removed the user");
                }

        }

        public static void FindMembership()
        {
                System.out.print("Enter phone Number: ");
                String phoneNumber = scanner.nextLine();

                if (!Utils.ValidatePhoneNumber(phoneNumber))
                {
                        System.out.println("Invalid phone number format. It must start with +");
                        return;
                } else phoneNumber = Utils.FormatPhoneNumberForQuery(phoneNumber);

                var mem = MembershipOptions.GetMembershipWithPhoneNumber(phoneNumber);
                if (mem != null)
                {
                        MembershipOptions.DisplayMembership(mem);
                } else
                {
                        System.out.println("No membership found with phone number " + phoneNumber);
                }
        }

        public static void AddMembership()
        {
                String phoneNumber, membershipType;
                System.out.print("User's phone number: ");
                phoneNumber = scanner.nextLine();

                if (!Utils.ValidatePhoneNumber(phoneNumber))
                {
                        System.out.println("Invalid phone number format. It must start with +");
                        return;
                } else phoneNumber = Utils.FormatPhoneNumberForQuery(phoneNumber);

                System.out.print("Membership type: ");
                membershipType = scanner.nextLine();

                boolean result = MembershipOptions.CreateMembership(phoneNumber, membershipType);
                if (result) System.out.println("Added membership");
                else System.out.println("Couldn't add membership");
        }

        public static void RemoveMembership()
        {
                System.out.print("Enter phone Number: ");
                String phoneNumber = scanner.nextLine();
                if (!Utils.ValidatePhoneNumber(phoneNumber))
                {
                        System.out.println("Invalid phone number format. It must start with +");
                        return;
                } else phoneNumber = Utils.FormatPhoneNumberForQuery(phoneNumber);

                boolean result = MembershipOptions.RemoveMembership(phoneNumber);
                if (result) System.out.println("Removed membership");
                else System.out.println("Couldn't remove membership");
        }

        public static void main(String[] args)
        {
                String option = "";

                do
                {
                        System.out.print("> ");
                        option = scanner.nextLine().trim().toLowerCase();

                        switch (option)
                        {
                                case "exit":
                                        break;

                                case "help":
                                        ShowHelp();
                                        break;

                                case "display_users":
                                        UserOptions.DisplayUsers();
                                        break;

                                case "find_user":
                                        FindUser();
                                        break;

                                case "add_user":
                                        AddUser();
                                        break;

                                case "remove_user":
                                        RemoveUser();
                                        break;

                                case "find_membership":
                                        FindMembership();
                                        break;

                                case "add_membership":
                                        AddMembership();
                                        break;

                                case "remove_membership":
                                        RemoveMembership();
                                        break;

                                default:
                                        System.out.println("Invalid option");
                                        break;
                        }

                } while (!option.equalsIgnoreCase("exit"));
        }
}