Given(/^I am on the start page for the form$/) do
  visit config['example_app_host']
end

Then(/^I can see the local form$/) do
  expect(page).to have_content 'hit'
  expect(page).to have_content 'Submit'
end

# When(/^I complete the local form incorrectly$/) do
#   fill_in "example-dob-day", :with => 'ff'
#   fill_in "example-dob-month", :with => 'ff'
#   fill_in "example-dob-year", :with => 'ff'
#   fill_in "example-email", :with => 'notvalid'
#   click_button("Continue")
# end
#
# Then(/^I am presented with validation errors for the local form$/) do
#   expect(page).to have_content 'Tell us which is your favourite superhero'
#   expect(page).to have_content 'Date must only contain numbers'
#   expect(page).to have_content 'Enter your favourite colour'
#   expect(page).to have_content 'The email address isn\'t valid, enter a valid email address'
# end

When(/^I complete the local form correctly$/) do
  fill_in "hit", :with => 'Duran Duran'
  fill_in "amountInPence", :with => '10'
  click_button("Submit")
end

Then(/^I am taken to the payment form$/) do
  expect(page).to have_content 'Example service'
  expect(page).to have_content 'Pay by card'
end

When(/^I enter my payment details$/) do
  fill_in "cardholderName", :with => 'Billy-Bob Jones'
  fill_in "cvs", :with => '4444333322221111'
  fill_in "expiryDate", :with => '11/23'
  fill_in "addressLine1", :with => 'My House'
  fill_in "addressLine1", :with => 'My Street'
  fill_in "addressCity", :with => 'My Town'
  fill_in "addressPostcode", :with => 'MY1 3PC'
  click_button("Make payment")
end

Then(/^I am taken to the payment confirmation page$/) do
  expect(page).to have_content 'Check your details are correct before confirming'
end

When(/^I confirm my payment$/) do
  click_button("Confirm")
end

Then(/^I am taken to the local success page$/) do
  expect(current_url).to equal 'http://localhost:9000/created'
  expect(page).to have_content 'Success'
end

