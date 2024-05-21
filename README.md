
<p align="center"><a href="https://paysky.io/" target="_blank"><img width="440" src="https://paysky.io/wp-content/uploads/2023/12/Paysky-logo.png"></a></p>



# PaySky PayButton SDK 
The PayButton helps make the integration of card acceptance into your app easy.

You simply provide the merchant information you receieve from PaySky to the payment SDK. The PayButton displays a ready-made view that guides the merchant through the payment process and shows a Summary screen at the end of the transaction.
​

### Getting Started
These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.
​
### Prerequisites

1. JDK installed on your machine with minimum version 1.7
2. Android Studio installed on your machine
3. Create a new Android project in Android Studio to use SDK or if you have created a project before with minSdkVersion API version 17.                 
4. AndroidX compatibility


## :computer: Installation
1. Open your android project.
2. In your project in build.gradle file in project level in allproject{} inside it you will find repositories{} inside it add
```
maven { url 'https://jitpack.io' }
```
3. In your build.gradle file in app level in dependencies{} add
```
implementation 'com.github.payskyCompany:paybutton-sdk:2.0.14'
```
4. Sync your project.

Note: Version  may not be the last version check Releases in github to get latest version.

## :rocket: Deployment
1. Before deploying your project live, you should get a merchant ID, terminal ID and Secure Hash Key from our company.
2. You should keep your merchant ID and terminal ID secured in your project, encrypt them before save them in project.
​
## :hammer_and_wrench: How to use
In order to use the SDK you should get a Merchant ID, a Terminal ID and Secure Hash Key from PaySky company.
​
### :point_right: Create a new instance from PayButton
In the class you want to intiate the payment from  PayButton
```java
PayButton payButton = new PayButton(context);
```
​
You need to just pass some parameters to PayButton class instance
1) Merchant id.
2) Terminal id.
3) Payment amount.
4) Currency code. (https://www.iban.com/currency-codes)
5) Secure hash key.
6) Transaction Reference Number. [Optional]
7) If the user is not subscribed, you will need to pass the customer's mobile number or email.
   Otherwise, the user is subscribed so you will pass by the customer ID.

Note That:
You shoud keep your secure hash key and merchant id and terminal id with encryption 
before save them in storage if you want.
​

If the merchant is *Not Subscribed*, and the channel selected is *Mobile Number*:-
```java
payButton.setMerchantId(merchantId); // Merchant id
payButton.setTerminalId(terminalId); // Terminal  id
payButton.setPayAmount(amount); // Amount
payButton.setCurrencyCode(currencyCode); // Currency code
payButton.setMerchantSecureHash("secure_hash_key"); // Secure hash key
payButton.setProductionStatus(PRODUCTION);                                   // for testing environment use GREY
// you can get reference number from AppUtils.generateRandomNumber();   
payButton.setTransactionReferenceNumber(AppUtils.generateRandomNumber());    // unique reference number [Optional]
payButton.setCustomerMobile("xxxxxxxxxx");
```

If the merchant is *Not Subscribed*, and the channel selected is *Email*:-
```java
payButton.setMerchantId(merchantId); // Merchant id
payButton.setTerminalId(terminalId); // Terminal  id
payButton.setPayAmount(amount); // Amount
payButton.setCurrencyCode(currencyCode); // Currency code
payButton.setMerchantSecureHash("secure_hash_key"); // Secure hash key
payButton.setProductionStatus(PRODUCTION);                                   // for testing environment use GREY
// you can get reference number from AppUtils.generateRandomNumber();   
payButton.setTransactionReferenceNumber(AppUtils.generateRandomNumber());    // unique reference number [Optional]
payButton.setCustomerEmail("joe@name.com");
```

If the merchant is *Subscribed*:-
```java
payButton.setMerchantId(merchantId); // Merchant id
payButton.setTerminalId(terminalId); // Terminal  id
payButton.setPayAmount(amount); // Amount
payButton.setCurrencyCode(currencyCode); // Currency code
payButton.setMerchantSecureHash("secure_hash_key"); // Secure hash key
payButton.setProductionStatus(PRODUCTION);                                   // for testing environment use GREY
// you can get reference number from AppUtils.generateRandomNumber();   
payButton.setTransactionReferenceNumber(AppUtils.generateRandomNumber());    // unique reference number [Optional]
payButton.setCustomerId("xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx");
```

### :luggage: Features
In order to create a transaction call
​
```java 
payButton.createTransaction(new PayButton.PaymentTransactionCallback() {
     @Override
     public void onCardTransactionSuccess(SuccessfulCardTransaction cardTransaction) {
         paymentStatusTextView.setText(cardTransaction.toString());
         Log.v("Transaction reference: ", cardTransaction.SystemReference); // Transaction reference number.
         Log.v("Customer Id: ", cardTransaction.tokenCustomerId); // Customer token ID.
     }
            
     @Override
     public void onWalletTransactionSuccess(SuccessfulWalletTransaction walletTransaction) {
          paymentStatusTextView.setText(walletTransaction.toString());
          walletTransaction.NetworkReference ; // transaction reference number.
     }
            
     @Override
     public void onError(TransactionException error) {
          paymentStatusTextView.setText("failed by:- " + error.getMessage());
     }
});
```
​
to create a transaction in our SDK you just call createTransaction method and pass to it
PaymentTransactionCallback listener to call it after transaction.
This listener has 3 methods:
​
1) `onCardTransactionSuccess`                                                                 
      this method called in case transaction success by card payment with SuccessfulCardTransaction object.
      SuccessfulCardTransaction object from create transaction listener contains:
      NetworkReference variable that is reference number of transaction.
      AuthCode variable
      ActionCode variable.
      ReceiptNumber variable.
      amount variable.
​  
2) `onWalletTransactionSuccess`                                                                    
      this method is called if customer make a wallet transaction with SuccessfulWalletTransaction object.
      SuccessfulWalletTransaction object from create transaction listener contains:
      NetworkReference variable that is reference number of transaction.
      amount variable.
​    
3) `onError`                                                       
in case transaction failed with TransactionException exception that has error info.                                                                                
​

### 🚀 Resolving conflicts

Because we use some of libraries like Okhttp, Retrofit, EventBus and you may use them with different version number. In some cases this will make a conflict in build project to solve this problem you should force the library that has conflict
with specific version number.

Example conflict in Gson library we use version  
```
implementation 'com.google.code.gson:gson:2.8.5'
```
and in your build.gradle file
```
implementation 'com.google.code.gson:gson:2.8.4'
```
**Solution:**

In your build.gradle file, add at the end of the file
```
configurations.all {
    resolutionStrategy { 
        force 'com.google.code.gson:gson:2.8.4'
    }
}
```
to force your specific version and sync project again.
 
Reference: https://stackoverflow.com/questions/28444016/how-can-i-force-gradle-to-set-the-same-version-for-two-dependencies/39292202
​
​
​
## :hammer_and_wrench: Built With
* [Retrofit](http://square.github.io/retrofit/) - Android Networking library.
* [EventBus](https://github.com/greenrobot/EventBus) - Event bus send events between your classes.  
​
​
## :writing_hand: Authors
**PaySky Company** - (https://www.paysky.io)
