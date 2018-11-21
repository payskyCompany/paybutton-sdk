## PaySky Button SDK

The purpose of this SDK to help programmers to use PaySky payment SDK .

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

What things you need to install the software and how to install them

```
Create new Android project in Android Studio or if you have project already.
```

### Installing

A step by step that tell you how to get our SDK in your project.

```
1- open your android project.
2- in your project in build.gradle file in project level in allproject{} inside it you will find repositories{} inside it add :-
maven { url 'https://jitpack.io' }
Example:-
allprojects { repositories {
     maven { url 'https://jitpack.io' }
                           }
            }
3- in your build.gradle file in app level in dependencies{} add :- implementation 'com.github.payskyCompany:fabsdk:1.1.10'
Example:-
dependencies {
  implementation 'com.github.payskyCompany:paybutton:1.1.12'
}
4- Sync your project.

Note:- 1.1.9 may not be the last version check Releases in github to get latest version.
```
### Using SDK

```
in order to use our SDK you should get merchant id and Terminal id from our company.

1 – Attach to your design xml file our Button.

  <io.paysky.ui.custom.PayButton
                android:id="@+id/paybtn"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:gravity="center" />

2 - if you don't have a xml view layour file you can add our PayButton in your java code.

  PayButton payButton = new PayButton(context);

3 - after inflate PayButton in xml find it in your java code like:-

  PayButton payButton = findViewByI(R.id.paybtn);

you need to just pass some parameters to our button to know merchant data and amount this parameters is:-
  1-Merchat id
  2-Terminal id
  3-Payment amount
  4-Currency code

Example:-

payButton.setMerchantId(merchantId); // Merchant id
payButton.setTerminalId(terminalId); // Terminal  id
payButton.setPayAmount(amount); // Amount
payButton.setCurrencyCode(currencyCode); // Currency Code

4 - in order to create transaction call:-

payButton.createTransaction(new PayButton.PaymentTransactionCallback() {
                    @Override
                    public void onSuccess(String referenceNumber,
                     String responseCode, String authorizationCode) {
                       // handle success transaction.
                    }

                    @Override
                    public void onError(Throwable error) {
                         // fail to create transacion and receive error.
                    }
                });

to create transaction in our sdk you just call createTransaction method and pass to it
PaymentTransactionCallback listener to call it after transaction.
this listener has 2 methods:-
  1 - onSuccess method
      this method called in case transaction success and you will receive  referenceNumber,  responseCode,  authorizationCode
      of transaction.
  2 - onError method in case transaction failed with Throwable exception that has error info.

Example:-

payButton.createTransaction(new PayButton.PaymentTransactionCallback() {
                    @Override
                    public void onSuccess(String referenceNumber, String responseCode,
                                         String authorizationCode) {
                       // handle success transaction.
                    }

                    @Override
                    public void onError(Throwable error) {
                         // fail to create transacion and receive error.
                    }
                });

```
## Deployment

1-Before deploy your project live ,you should get a merchant id and terminal id from our company .
2-you should keep your merchant id and terminal id secured
in your project, encrypt them before save them in project.

## Built With

* [Retrofit](http://square.github.io/retrofit/) - Android Networking library.
* [https://github.com/greenrobot/EventBus) - Event bus send events between your classes.


## Authors

**PaySky Company** - (https://www.paysky.io)

## Sample Project
**https://github.com/payskyCompany/payButton.git**



