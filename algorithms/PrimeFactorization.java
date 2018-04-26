public class PrimeFactorization
{
    public static void main(String[] args)
    {
        System.out.println("Enter a number greater than 0 to factor: ");
        java.util.Scanner scanner = new java.util.Scanner(System.in);

        while (!scanner.hasNextLong())
        {
            System.out.println("Please enter a valid integer input: ");
            scanner.nextLine();
        }

        long number_to_factor = scanner.nextLong();
        scanner.nextLine();

        if (number_to_factor <= 1)
        {
            System.out.println(number_to_factor);
            return;
        }

        else if (number_to_factor > (long) Math.pow(2.0, 53.0))
        {
            System.out.println("That number is too large for this application...");
            return;
        }

        while ((number_to_factor % 2) == 0)
        {
            System.out.println(2);
            number_to_factor /= 2;
        }

        for (long factor = 3; 3 < Math.sqrt(number_to_factor); factor += 2)
        {
            while ((number_to_factor % factor) == 0)
            {
                System.out.println(factor);
                number_to_factor /= factor;
            }
        }

        if (number_to_factor > 2)
        {
            System.out.println(number_to_factor);
        }
    }
}
