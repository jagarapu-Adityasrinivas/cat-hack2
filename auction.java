import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class User {
    private String username;

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}

class Auction {
    private String item;
    private double startingBid;
    private double currentBid;
    private List<Bid> bids;
    private boolean isActive;

    public Auction(String item, double startingBid) {
        this.item = item;
        this.startingBid = startingBid;
        this.currentBid = startingBid;
        this.bids = new ArrayList<>();
        this.isActive = true;
    }

    public String getItem() {
        return item;
    }

    public double getCurrentBid() {
        return currentBid;
    }

    public boolean placeBid(User bidder, double bidAmount) {
        if (isActive && bidAmount > currentBid) {
            bids.add(new Bid(bidder, bidAmount));
            currentBid = bidAmount;
            return true;
        }
        return false;
    }

    public void endAuction() {
        isActive = false;
    }

    public boolean isActive() {
        return isActive;
    }

    public List<Bid> getBids() {
        return bids;
    }
}

class Bid {
    private User bidder;
    private double amount;

    public Bid(User bidder, double amount) {
        this.bidder = bidder;
        this.amount = amount;
    }

    public User getBidder() {
        return bidder;
    }

    public double getAmount() {
        return amount;
    }
}

class AuctionSystem {
    private List<User> users;
    private List<Auction> auctions;

    public AuctionSystem() {
        users = new ArrayList<>();
        auctions = new ArrayList<>();
        createDefaultAuctions();
    }

    public void registerUser(String username) {
        users.add(new User(username));
    }

    public User findUser(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public void createAuction(String item, double startingBid) {
        auctions.add(new Auction(item, startingBid));
    }

    public List<Auction> getActiveAuctions() {
        List<Auction> activeAuctions = new ArrayList<>();
        for (Auction auction : auctions) {
            if (auction.isActive()) {
                activeAuctions.add(auction);
            }
        }
        return activeAuctions;
    }

    private void createDefaultAuctions() {
        auctions.add(new Auction("Antique Vase", 100.0));
        auctions.add(new Auction("Vintage Car", 5000.0));
        auctions.add(new Auction("Rare Painting", 1500.0));
    }
}

public class OnlineAuction {
    public static void main(String[] args) {
        AuctionSystem auctionSystem = new AuctionSystem();
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your username to register: ");
        String username = scanner.nextLine();
        auctionSystem.registerUser(username);
        System.out.println("Registration successful! Welcome, " + username + "!");

        boolean continueSession = true;
        while (continueSession) {
            System.out.print("Are you here to (1) Conduct an auction or (2) Participate in an auction? (Enter 1 or 2, or 0 to exit): ");
            int choice = scanner.nextInt();

            if (choice == 1) {
                System.out.print("Enter the item name: ");
                scanner.nextLine();
                String item = scanner.nextLine();
                System.out.print("Enter the starting bid: ");
                double startingBid = scanner.nextDouble();
                auctionSystem.createAuction(item, startingBid);
                System.out.println("Auction created for item: " + item + " with starting bid: " + startingBid);
            } else if (choice == 2) {
                System.out.print("Enter the number of bidders: ");
                int numberOfBidders = scanner.nextInt();
                List<User> bidders = new ArrayList<>();

                for (int i = 0; i < numberOfBidders; i++) {
                    System.out.print("Enter username for bidder " + (i + 1) + ": ");
                    scanner.nextLine();
                    String bidderUsername = scanner.nextLine();
                    auctionSystem.registerUser(bidderUsername);
                    bidders.add(auctionSystem.findUser(bidderUsername));
                }

                System.out.println("Active Auctions:");
                List<Auction> activeAuctions = auctionSystem.getActiveAuctions();
                for (int i = 0; i < activeAuctions.size(); i++) {
                    Auction auction = activeAuctions.get(i);
                    System.out.println((i + 1) + ". Item: " + auction.getItem() + ", Current Bid: " + auction.getCurrentBid());
                }

                for (Auction auction : activeAuctions) {
                    boolean continueBidding = true;
                    while (continueBidding) {
                        for (User bidder : bidders) {
                            System.out.print(bidder.getUsername() + ", enter your bid for " + auction.getItem() + " (or -1 to end the auction): ");
                            double bidAmount = scanner.nextDouble();

                            if (bidAmount == -1) {
                                auction.endAuction();
                                System.out.println("Auction ended for item: " + auction.getItem());
                                continueBidding = false;
                                break;
                            }

                            if (auction.placeBid(bidder, bidAmount)) {
                                System.out.println("Bid placed successfully!");
                            } else {
                                System.out.println("Bid failed. Ensure it's higher than the current bid or auction is active.");
                            }
                        }
                    }
                }
            } else if (choice == 0) {
                continueSession = false;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }

        System.out.println("Exiting the auction system. Goodbye!");
        scanner.close();
    }
}
