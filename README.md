# CUBA Petclinic Example: Using Sneferu

<p align="center">
  <img src="https://github.com/cuba-platform/cuba-petclinic/blob/master/modules/web/themes/hover/branding/petclinic_logo_full.png"/>
</p>


This repository contains example test cases that show how to use [Sneferu](https://github.com/cuba-platform/sneferu)
through the example application of the CUBA Petclinic.

### Test Cases

The different test cases can be found in the test directory of the web module: [modules/web/test/com/haulmont/sample/petclinic/web](https://github.com/cuba-platform/cuba-petclinic-using-sneferu/tree/master/modules/web/test/com/haulmont/sample/petclinic/web)

Those test cases show different usages of Sneferu. 


### JUnit 5

An example using JUnit 5 can be found in the [VisitEditTest.java](https://github.com/cuba-platform/cuba-petclinic-using-sneferu/blob/master/modules/web/test/com/haulmont/sample/petclinic/web/visit/VisitEditTest.java)

```java

public class VisitEditTest {

  @RegisterExtension
  public TestUiEnvironment environment =
      new TestUiEnvironment(PetclinicWebTestContainer.Common.INSTANCE)
          .withScreenPackages(
              "com.haulmont.cuba.web.app.main",
              "com.haulmont.sample.petclinic.web"
          )
          .withUserLogin("admin");



  private UiTestAPI uiTestAPI;

  private StandardEditorTestAPI<VisitEdit> visitEdit;

  private Pet pikachu;

  @BeforeEach
  public void setupEnvironment() {
    uiTestAPI = new CubaWebUiTestAPI(
        environment,
        AppBeans.get(ScreenBuilders.class),
        MainScreen.class
    );

    StandardLookupTestAPI<VisitBrowse> visitBrowse = uiTestAPI
        .openStandardLookup(Visit.class, VisitBrowse.class);

    visitBrowse.interact(click(button("createBtn")));

    visitEdit = uiTestAPI.getOpenedEditorScreen(VisitEdit.class);


    Metadata metadata = AppBeans.get(Metadata.class);
    pikachu = metadata.create(Pet.class);
    pikachu.setName("Pikachu");

  }

  @Test
  public void aVisitCanBeCreated_whenAllFieldsAreFilled() {

    // when:
    OperationResult outcome = (OperationResult) visitEdit
        .interact(enter(dateField("visitDateField"), new Date()))
        .interact(enter(textField("descriptionField"), "Regular Visit"))
        .interact(select(lookupField("petField"), pikachu))
        .andThenGet(closeEditor());

    // then:
    assertThat(outcome).isEqualTo(OperationResult.success());

    // and:
    assertThat(uiTestAPI.isActive(visitEdit))
      .isFalse();
  }

  @Test
  public void aVisitCannotBeCreated_whenVisitDateIsMissing() {

    // when:
    visitEdit
        .interact(enter(dateField("visitDateField"), null));

    // and:
    OperationResult outcome = (OperationResult) visitEdit
        .interact(enter(textField("descriptionField"), "Regular Visit"))
        .interact(select(lookupField("petField"), pikachu))
        .andThenGet(closeEditor());

    // then:
    assertThat(outcome).isEqualTo(OperationResult.fail());

    // and:
    assertThat(visitEdit.screen().validateScreen().getAll())
      .hasSize(1);
  }

  @AfterEach
  public void closeAllScreens() {
      uiTestAPI.closeAllScreens();
      TestServiceProxy.clear();
  }
}
```


#### Spock Example

[CreateOwnerSpec.groovy](https://github.com/cuba-platform/cuba-petclinic-using-sneferu/blob/master/modules/web/test/com/haulmont/sample/petclinic/web/owner/CreateOwnerSpec.groovy)

````groovy
class CreateOwnerSpec extends PetclinicWebIntegrationSpec {

  def "an Owner can be created when all attributes are provided in the Editor"() {

    given:
    def ownerEdit = OwnerEditScreenObject.newEntity(uiTestAPI)

    when:
    OperationResult operationResult = ownerEdit
        .saveOwnerWithDetails(
            "Ash",
            "Ketchum",
            "Miastreet 134",
            "Alabastia")

    then:
    operationResult == OperationResult.success()
  }

  def "an Owner cannot be created when first name is missing"() {

    given:
    def ownerEdit = OwnerEditScreenObject.newEntity(uiTestAPI)

    when:
    OperationResult operationResult = ownerEdit
        .saveOwnerWithDetails(
            null,
            "Ketchum",
            "Miastreet 134",
            "Alabastia")

    then:
    operationResult == OperationResult.fail()
  }
}
````

##### Spock using Screen Objects

There is also an example on how to use [ScreenObjects](https://github.com/cuba-platform/sneferu#screen-objects).

The Screen Object for the OwnerEdit screen looks like this:
```java
public class OwnerEditScreenObject implements ScreenObject<StandardEditorTestAPI<OwnerEdit>> {

  private StandardEditorTestAPI<OwnerEdit> delegate;

  static OwnerEditScreenObject newEntity(
      UiTestAPI uiTestAPI
  ) {

    StandardEditorTestAPI<OwnerEdit> standardLookupTestAPI = uiTestAPI.openStandardEditor(
        Owner.class,
        OwnerEdit.class
    );

    return new OwnerEditScreenObject(
        standardLookupTestAPI
    );
  }


  static OwnerEditScreenObject forEntity(
      UiTestAPI uiTestAPI,
      Owner entity
  ) {

    StandardEditorTestAPI<OwnerEdit> standardLookupTestAPI = uiTestAPI.openStandardEditor(
        Owner.class,
        OwnerEdit.class,
        entity
    );

    return new OwnerEditScreenObject(
        standardLookupTestAPI
    );
  }


  private OwnerEditScreenObject(
      StandardEditorTestAPI<OwnerEdit> delegate) {
    this.delegate = delegate;
  }

  @Override
  public StandardEditorTestAPI<OwnerEdit> delegate() {
    return delegate;
  }

  public OperationResult saveOwnerWithDetails(
      String firstName,
      String lastName,
      String address,
      String city
  ) {
    return (OperationResult) delegate
        .interact(enter(textField("firstNameField"), firstName))
        .andThen(enter(textField("lastNameField"), lastName))
        .andThen(enter(textField("addressField"), address))
        .andThen(enter(textField("cityField"), city))
        .andThenGet(closeEditor());
  }

  public boolean addressIsSetTo(String expectedAddress) {
    return delegate.component(textField("addressField"))
        .rawComponent()
        .getValue().equals(expectedAddress);
  }
}

```

Its usage in a Spock based test case:

```groovy
class EditOwnerSpec extends PetclinicWebIntegrationSpec {

  private Owner existingOwner

  def setup() {
    existingOwner = metadata.create(Owner)
  }

  def "the address attribute of the Owner entity is correctly bound to the Address Field"() {

    given:
    existingOwner.address = "Miastreet 134"

    and:
    def ownerEdit = OwnerEditScreenObject.forEntity(uiTestAPI, existingOwner)

    expect:
    ownerEdit.addressIsSetTo("Miastreet 134")
  }

  def "the city attribute is correctly bound to the city field"() {

    given:
    existingOwner.city = "Alabastia"

    and:
    def ownerEdit = OwnerEditScreenObject.forEntity(uiTestAPI, existingOwner)

    expect:
    def city = ownerEdit.delegate()
                        .verify(componentValue(textField("cityField")))

    city == "Alabastia"
  }
}
```