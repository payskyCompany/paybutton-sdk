## Version 2.0.5 adding Support for AndroidX 

## PaySky Button SDK

The purpose of this SDK to help programmers to use PaySky payment SDK .

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

What things you need to install the software and how to install them

```
1-JDK installed on your machine with minimum version 1.7 .
2-Android Studio
3-Create new Android project in Android Studio to use SDK or if you have created a project before with minSdkVersion api version 17.
4- AndroidX compatibility
```

### Installing

A step by step that tell you how to get our SDK in your project.

```
1- open your android project.
2- in your project in build.gradle file in project level in allproject{} inside it you will find
repositories{} inside it add :-
maven { url 'https://jitpack.io' }

Example:-
     allprojects { repositories {
         maven { url 'https://jitpack.io' }
                               }
                }
			  
3- in your build.gradle file in app level in dependencies{} add :-    
implementation 'com.github.payskyCompany:paybutton-sdk:2.0.5'

Example:-
dependencies {
      implementation 'com.github.payskyCompany:paybutton-sdk:2.0.5'
}

4- Sync your project.

Note:- version 2.0.5 may not be the last version check Releases in github to get latest version.
```

### Using SDK

```
in order to use our SDK you should get merchant id and Terminal id from our company.

1 – create a new instance from PayButton:-  

PayButton payButton = new PayButton(context);

you need to just pass some parameters to PayButton class instance :-
  1-Merchat id.
  2-Terminal id.
  3-Payment amount.
  4-Currency code [Optional].
  5-merchant secure hash.
  6-transaction Reference Number.
  
Note That:-
you shoud keep your secure hash and merchant id and terminal id with encryption 
before save them in storage if you want.

Example:-

payButton.setMerchantId(merchantId); // Merchant id
payButton.setTerminalId(terminalId); // Terminal  id
payButton.setPayAmount(amount); // Amount
payButton.setCurrencyCode(currencyCode); // Currency Code [Optional]
payButton.setMerchantSecureHash("Merchant secure hash"); // Merchant secure hash
payButton.setTransactionReferenceNumber("reference number"); // unique transaction reference number.
payButton.setProductionStatus(PRODUCTION); // for testing environment use GREY
// you can get reference number from AppUtils.generateRandomNumber();
example:-        
payButton.setTransactionReferenceNumber(AppUtils.generateRandomNumber());
       
2 - in order to create transaction call:-

payButton.createTransaction(new PayButton.PaymentTransactionCallback() {

                    @Override
                    public void onCardTransactionSuccess(SuccessfulCardTransaction cardTransaction) {
                        paymentStatusTextView.setText(cardTransaction.toString());
                    }

                    @Override
                    public void onWalletTransactionSuccess(SuccessfulWalletTransaction walletTransaction) {
                        paymentStatusTextView.setText(walletTransaction.toString());
                    }

                    @Override
                    public void onError(Throwable error) {
                        paymentStatusTextView.setText("failed by:- " + error.getMessage());
                    }
                });

to create transaction in our sdk you just call createTransaction method and pass to it
PaymentTransactionCallback listener to call it after transaction.
this listener has 2 methods:-

  1 - onCardTransactionSuccess method
      this method called in case transaction success by card payment with SuccessfulCardTransaction object.
      SuccessfulCardTransaction object from create transaction listener contains:-
      NetworkReference variable that is reference number of transaction.
      AuthCode variable
      ActionCode variable.
      ReceiptNumber variable.
      amount variable.
      
  2 - onWalletTransactionSuccess method 
      this method is called if customer make a wallet transaction with SuccessfulWalletTransaction object.
      SuccessfulWalletTransaction object from create transaction listener contains:-
      NetworkReference variable that is reference number of transaction.
      amount variable.
      
  3- onError method in case transaction failed with Throwable exception that has error info.
  
  
Example:- 

          payButton.createTransaction(new PayButton.PaymentTransactionCallback() {
                    @Override
                    public void onCardTransactionSuccess(SuccessfulCardTransaction cardTransaction) {
                        paymentStatusTextView.setText(cardTransaction.toString());
			cardTransaction.NetworkReference; // transaction reference number.
                    }

                    @Override
                    public void onWalletTransactionSuccess(SuccessfulWalletTransaction walletTransaction) {
                        paymentStatusTextView.setText(walletTransaction.toString());
			walletTransaction.NetworkReference ; // transaction reference number.
                    }

                    @Override
                    public void onError(Throwable error) {
                        paymentStatusTextView.setText("failed by:- " + error.getMessage());
                    }
                });
            }
        });

```

### Resolving conflict

Because we use some of libraries like Okhttp, retrofit , EventBus and you may use them with different version number ,
in some cases this will make a conflict in build project to solve this problem you should force the library that has conflict
with specific version number.
```
Example conflict in Gson library we use version  
implementation 'com.google.code.gson:gson:2.8.5'
and in your build.gradle file
implementation 'com.google.code.gson:gson:2.8.4'
in your build.gradle file add in bottom of it:-

configurations.all {
    resolutionStrategy { 
        force 'com.google.code.gson:gson:2.8.4'
    }
}

force your specific version and sync project again.
```
 Reference for how to solve problem
(https://stackoverflow.com/questions/28444016/how-can-i-force-gradle-to-set-the-same-version-for-two-dependencies)
(https://stackoverflow.com/questions/28444016/how-can-i-force-gradle-to-set-the-same-version-for-two-dependencies/39292202)


## Deployment

1-Before deploy your project live ,you should get a merchant id and terminal id from our company .
2-you should keep your merchant id and terminal id secured
in your project, encrypt them before save them in project.

## Built With

* [Retrofit](http://square.github.io/retrofit/) - Android Networking library.
* [EventBus](https://github.com/greenrobot/EventBus) - Event bus send events between your classes.


## Authors

**PaySky Company** - (https://www.paysky.io)

## Sample Project
**https://github.com/payskyCompany/payButton.git**


