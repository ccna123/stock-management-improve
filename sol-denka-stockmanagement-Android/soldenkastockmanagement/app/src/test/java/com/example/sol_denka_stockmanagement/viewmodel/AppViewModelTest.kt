package com.example.sol_denka_stockmanagement.viewmodel

import android.content.Context
import app.cash.turbine.test
import com.example.sol_denka_stockmanagement.MainDispatcherRule
import com.example.sol_denka_stockmanagement.app_interface.IPresetRepo
import com.example.sol_denka_stockmanagement.constant.Category
import com.example.sol_denka_stockmanagement.constant.ConnectionState
import com.example.sol_denka_stockmanagement.constant.InboundInputField
import com.example.sol_denka_stockmanagement.constant.PackingType
import com.example.sol_denka_stockmanagement.constant.ProcessMethod
import com.example.sol_denka_stockmanagement.constant.WinderType
import com.example.sol_denka_stockmanagement.constant.generateTimeStamp
import com.example.sol_denka_stockmanagement.database.repository.csv.CsvHistoryRepository
import com.example.sol_denka_stockmanagement.database.repository.csv.CsvTaskTypeRepository
import com.example.sol_denka_stockmanagement.database.repository.field.ItemTypeFieldSettingMasterRepository
import com.example.sol_denka_stockmanagement.database.repository.item.ItemCategoryRepository
import com.example.sol_denka_stockmanagement.database.repository.item.ItemTypeRepository
import com.example.sol_denka_stockmanagement.database.repository.location.LocationMasterRepository
import com.example.sol_denka_stockmanagement.database.repository.process.ProcessTypeRepository
import com.example.sol_denka_stockmanagement.database.repository.winder.WinderInfoRepository
import com.example.sol_denka_stockmanagement.helper.NetworkConnectionObserver
import com.example.sol_denka_stockmanagement.helper.controller.ReaderController
import com.example.sol_denka_stockmanagement.helper.csv.CsvHelper
import com.example.sol_denka_stockmanagement.helper.toast.ToastType
import com.example.sol_denka_stockmanagement.intent.InputIntent.ChangeCategory
import com.example.sol_denka_stockmanagement.intent.InputIntent.UpdateFieldErrors
import com.example.sol_denka_stockmanagement.intent.InputIntent.ChangeLocation
import com.example.sol_denka_stockmanagement.intent.InputIntent.ChangeProcessMethod
import com.example.sol_denka_stockmanagement.model.item.ItemCategoryModel
import com.example.sol_denka_stockmanagement.model.item.ItemTypeMasterModel
import com.example.sol_denka_stockmanagement.model.location.LocationMasterModel
import com.example.sol_denka_stockmanagement.model.process.ProcessTypeModel
import com.example.sol_denka_stockmanagement.model.reader.ReaderInfoModel
import com.example.sol_denka_stockmanagement.model.winder.WinderInfoModel
import com.example.sol_denka_stockmanagement.reader.FakeBeeperVolume
import com.example.sol_denka_stockmanagement.reader.FakeChannel
import com.example.sol_denka_stockmanagement.reader.FakeInventoryState
import com.example.sol_denka_stockmanagement.reader.FakeSession
import com.example.sol_denka_stockmanagement.state.DialogState
import com.example.sol_denka_stockmanagement.state.ExpandState
import com.example.sol_denka_stockmanagement.state.GeneralState
import com.example.sol_denka_stockmanagement.state.InputState
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.math.exp
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class AppViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: AppViewModel

    private val context = mockk<Context>(relaxed = true)
    private val readerController = mockk<ReaderController>(relaxed = true)
    private val connectionObserver = mockk<NetworkConnectionObserver>(relaxed = true)
    private val locationRepo = mockk<LocationMasterRepository>(relaxed = true)
    private val csvTaskRepo = mockk<CsvTaskTypeRepository>(relaxed = true)
    private val csvHistoryRepo = mockk<CsvHistoryRepository>(relaxed = true)
    private val itemTypeRepo = mockk<ItemTypeRepository>(relaxed = true)
    private val fieldSettingRepo = mockk<ItemTypeFieldSettingMasterRepository>(relaxed = true)
    private val categoryRepo = mockk<ItemCategoryRepository>(relaxed = true)
    private val winderRepo = mockk<WinderInfoRepository>(relaxed = true)
    private val processRepo = mockk<ProcessTypeRepository>(relaxed = true)
    private val csvHelper = mockk<CsvHelper>(relaxed = true)

    private val presetRepos = emptySet<IPresetRepo>()
    private val locationFlow = MutableStateFlow<List<LocationMasterModel>>(emptyList())
    private val itemCategoryFlow = MutableStateFlow<List<ItemCategoryModel>>(emptyList())
    private val winderFlow = MutableStateFlow<List<WinderInfoModel>>(emptyList())
    private val processTypeFlow = MutableStateFlow<List<ProcessTypeModel>>(emptyList())
    private val readerInfoFlow = MutableStateFlow(ReaderInfoModel())
    private val connectionStateFlow = MutableStateFlow(ConnectionState.DISCONNECTED)
    private val isPerformingInventoryFlow = MutableStateFlow(false)
    private val connectionEventsFlow: MutableSharedFlow<ConnectionState> =
        MutableSharedFlow(extraBufferCapacity = 1)


    @Before
    fun setup() {
        every { readerController.readerInfo } returns readerInfoFlow
        every { readerController.connectionState } returns connectionStateFlow
        every { readerController.isPerformingInventory } returns isPerformingInventoryFlow
        every { readerController.connectionEvents } returns connectionEventsFlow

        every { connectionObserver.isConnected } returns MutableStateFlow(false)

        every { locationRepo.get() } returns locationFlow
        every { categoryRepo.get() } returns itemCategoryFlow
        every { winderRepo.get() } returns winderFlow
        every { processRepo.get() } returns processTypeFlow

        viewModel = AppViewModel(
            context,
            readerController,
            connectionObserver,
            locationRepo,
            csvTaskRepo,
            csvHistoryRepo,
            itemTypeRepo,
            fieldSettingRepo,
            categoryRepo,
            winderRepo,
            processRepo,
            presetRepos,
            csvHelper
        )
    }


    @Test
    fun `getGeneralState initial state verification`() {
        // Verify that `generalState` initially emits the default `GeneralState()`.
        assert(viewModel.generalState.value == GeneralState())
    }

    @Test
    fun `getExpandState initial state verification`() {
        // Verify that `expandState` initially emits the default `ExpandState()`.
        assert(viewModel.expandState.value == ExpandState())
    }

    @Test
    fun `getInputState initial state verification`() {
        // Verify that `inputState` initially emits the default `InputState()`.
        assert(viewModel.inputState.value == InputState())
    }

    @Test
    fun `getDialogState initial state verification`() {
        // Verify that `dialogState` initially emits `DialogState.Hidden`.
        assert(viewModel.dialogState.value == DialogState.Hidden)
    }

    @Test
    fun `getShowFileProgressDialog initial state verification`() {
        // Verify that `showFileProgressDialog` is initially `false`.
    }

    @Test
    fun `setShowFileProgressDialog state change check`() {
        // Verify that setting `showFileProgressDialog` to `true` or `false` updates its value correctly.
        // TODO implement test
    }

    @Test
    fun `getProgress initial value verification`() {
        // Verify that `progress` state flow initially emits `0f`.
        assert(viewModel.progress.value == 0f)
    }

    @Test
    fun `isFileWorking initial state verification`() {
        // Verify that `isFileWorking` state flow initially emits `false`.
        // TODO implement test
    }

    @Test
    fun `getToastFlow emission verification`() = runTest {
        // Test that emitting a message and type to `toastFlow` can be collected by a downstream collector.
        viewModel.toastFlow.test {
            viewModel.onInputIntent(
                UpdateFieldErrors(
                    errors = mapOf(InboundInputField.WIDTH.code to true)
                )
            )

            val toast = awaitItem()
            assert("必須項目を入力してください" == toast.first)
            assert(ToastType.ERROR == toast.second)
        }
    }

    @Test
    fun `getShowConnectingDialog initial state verification`() {
        // Verify that `showConnectingDialog` is initially `false`.
        // TODO implement test
    }

    @Test
    fun `getShowAppDialog initial state verification`() {
        // Verify that `showAppDialog` is initially `false`.
        // TODO implement test
    }

    @Test
    fun `getShowClearTagConfirmDialog initial state verification`() {
        // Verify that `showClearTagConfirmDialog` is initially `false`.
        // TODO implement test
    }

    @Test
    fun `getShowRadioPowerChangeDialog initial state verification`() {
        // Verify that `showRadioPowerChangeDialog` is initially `false`.
        // TODO implement test
    }

    @Test
    fun `getShowModalProcessMethod initial state verification`() {
        // Verify that `showModalProcessMethod` state flow initially emits `false`.
        // TODO implement test
    }


    @Test
    fun `getLocationMaster initial state and data loading`() = runTest {
        // Verify that `locationMaster` is initially an empty list and is later populated with data from the repository.
        assertTrue(viewModel.locationMaster.value.isEmpty())

        val fakeLocation = listOf(
            LocationMasterModel(
                locationId = 1,
                locationCode = "A-01",
                locationName = "倉庫A",
            )
        )
        locationFlow.value = fakeLocation
        advanceUntilIdle()

        assert(viewModel.locationMaster.value == fakeLocation)

    }

    @Test
    fun `getItemCategoryMaster initial state and data loading`() = runTest {
        // Verify that `itemCategoryMaster` is initially an empty list and is later populated with data from the repository.
        assertTrue(viewModel.itemCategoryMaster.value.isEmpty())

        val fakeCategory = listOf(
            ItemCategoryModel(
                itemCategoryId = 1,
                itemCategoryName = Category.SUB_MATERIAL.displayName,
                createdAt = generateTimeStamp(),
                updatedAt = generateTimeStamp(),
            ),
            ItemCategoryModel(
                itemCategoryId = 2,
                itemCategoryName = Category.SUB_RAW_MATERIAL.displayName,
                createdAt = generateTimeStamp(),
                updatedAt = generateTimeStamp(),
            ),
        )
        itemCategoryFlow.value = fakeCategory
        advanceUntilIdle()

        assert(viewModel.itemCategoryMaster.value == fakeCategory)
    }

    @Test
    fun `getWinderMaster initial state and data loading`() = runTest {
        // Verify that `winderMaster` is initially an empty list and is later populated with data from the repository.
        assertTrue(viewModel.winderMaster.value.isEmpty())

        val fakeWinder = listOf(
            WinderInfoModel(
                winderId = 1,
                winderName = WinderType.MACHINE_2.displayName,
                createdAt = generateTimeStamp(),
                updatedAt = generateTimeStamp()
            ),
            WinderInfoModel(
                winderId = 2,
                winderName = WinderType.SLITTING_B_F.displayName,
                createdAt = generateTimeStamp(),
                updatedAt = generateTimeStamp()
            ),
        )
        winderFlow.value = fakeWinder
        advanceUntilIdle()

        assert(viewModel.winderMaster.value == fakeWinder)
    }

    @Test
    fun `getProcessTypeMaster initial state and data loading`() = runTest {
        // Verify that `processTypeMaster` is initially an empty list and is later populated with data from the repository.
        assertTrue(viewModel.processTypeMaster.value.isEmpty())

        val fakeProcessType = listOf(
            ProcessTypeModel(
                processTypeId = 1,
                processCode = ProcessMethod.USE,
                processName = ProcessMethod.USE.displayName,
                createdAt = generateTimeStamp(),
                updatedAt = generateTimeStamp()
            ),
            ProcessTypeModel(
                processTypeId = 2,
                processCode = ProcessMethod.SALE,
                processName = ProcessMethod.SALE.displayName,
                createdAt = generateTimeStamp(),
                updatedAt = generateTimeStamp()
            ),
        )
        processTypeFlow.value = fakeProcessType
        advanceUntilIdle()

        assert(viewModel.processTypeMaster.value == fakeProcessType)
    }

    @Test
    fun `getOutboundProcessErrorSet initial state verification`() {
        // Verify that `outboundProcessErrorSet` is initially an empty set.
        assertTrue(viewModel.outboundProcessErrorSet.value.isEmpty())
    }

    @Test
    fun `getSearchResults initial state verification`() {
        // Verify that `searchResults` is initially an empty list.
        assertTrue(viewModel.searchResults.value.isEmpty())
    }

    @Test
    fun `getInboundInputFormResults initial state verification`() {
        // Verify that `inboundInputFormResults` is initially an empty list.
        assertTrue(viewModel.inboundInputFormResults.value.isEmpty())
    }

    @Test
    fun `getReaderInfo initial value verification`() = runTest {
        // Verify that `readerInfo` emits the initial `ReaderInfoModel()` value before the upstream flow provides a value.

        // force subscription
        val job = launch {
            viewModel.readerInfo.collect { }
        }

        assertTrue(viewModel.readerInfo.value == ReaderInfoModel())

        val newInfo = ReaderInfoModel(
            connectionState = ConnectionState.CONNECTED,
            readerName = "Fake Reader",
            batteryLevel = 100,
            radioPower = 30,
            radioPowerMw = 1000,
            buzzerVolume = FakeBeeperVolume.MEDIUM_BEEP,
            firmwareVer = "Fake Firmware",
            rfidSession = FakeSession.SESSION_S0,
            tagPopulation = 30,
            tagAccessFlag = FakeInventoryState.INVENTORY_STATE_A,
            rfidRadioIrradiation = 1,
            supportedChannels = listOf(
                FakeChannel.CHANNEL1,
                FakeChannel.CHANNEL2,
                FakeChannel.CHANNEL3,
                FakeChannel.CHANNEL4,
                FakeChannel.CHANNEL5,
                FakeChannel.CHANNEL6
            )
        )

        readerInfoFlow.value = newInfo

        advanceUntilIdle()

        assert(viewModel.readerInfo.value == newInfo)
        job.cancel()
    }

    @Test
    fun `getConnectionState initial value verification`() = runTest {
        // Verify that `connectionState` emits the initial `ConnectionState.DISCONNECTED` value before the upstream flow provides a value.
        val job = launch {
            viewModel.connectionState.collect { }
        }

        assertTrue(viewModel.connectionState.value == ConnectionState.DISCONNECTED)

        connectionStateFlow.value = ConnectionState.CONNECTED

        advanceUntilIdle()

        assertEquals(ConnectionState.CONNECTED, viewModel.connectionState.value)
        job.cancel()
    }

    @Test
    fun `isNetworkConnected initial value verification`() {
        // Verify that `isNetworkConnected` emits the initial `false` value.
        // TODO implement test
    }

    @Test
    fun `isPerformingInventory initial value verification`() = runTest {
        // Verify that `isPerformingInventory` emits the initial `false` value before the upstream flow provides a value.
        val job = launch {
            viewModel.isPerformingInventory.collect { }
        }

        assertTrue(!viewModel.isPerformingInventory.value)

        isPerformingInventoryFlow.value = true

        advanceUntilIdle()

        assertTrue(viewModel.isPerformingInventory.value)
        job.cancel()
    }

    @Test
    fun `connectionEvents CONNECTING shows connecting dialog`() = runTest {

        assertFalse(viewModel.showConnectingDialog.value)

        connectionEventsFlow.emit(ConnectionState.CONNECTING)

        advanceUntilIdle()

        assertTrue(viewModel.showConnectingDialog.value)
    }

    @Test
    fun `connectionEvents DISCONNECTED hides dialog and emits error toast`() = runTest {
        viewModel.toastFlow.test {
            connectionEventsFlow.emit(ConnectionState.DISCONNECTED)
            advanceUntilIdle()

            // dialog hidden
            assertFalse(viewModel.showConnectingDialog.value)

            val toast = awaitItem()
            assertEquals("リーダーが切断されました", toast.first)
            assertEquals(ToastType.ERROR, toast.second)
        }
    }

    @Test
    fun `connectionEvents CONNECTED hides dialog and emits success toast`() = runTest {

        viewModel.showConnectingDialog.value = true

        viewModel.toastFlow.test {
            connectionEventsFlow.emit(ConnectionState.CONNECTED)
            advanceUntilIdle()

            // dialog hidden
            assertFalse(viewModel.showConnectingDialog.value)

            val toast = awaitItem()
            assertEquals("リーダー接続に成功しました", toast.first)
            assertEquals(ToastType.SUCCESS, toast.second)
        }
    }

    @Test
    fun `onInputIntent   ChangeProcessMethod`() {
        // Verify that this intent correctly updates the `processMethod` in `inputState`.
        assertEquals(ProcessMethod.USE.displayName, viewModel.inputState.value.processMethod)

        viewModel.onInputIntent(ChangeProcessMethod(value = ProcessMethod.DISCARD.displayName))

        assertEquals(ProcessMethod.DISCARD.displayName, viewModel.inputState.value.processMethod)
    }

    @Test
    fun `onInputIntent   ChangeLocation`() {
        // Verify that this intent correctly updates the `location` in `inputState`.
        assertEquals("", viewModel.inputState.value.location)

        viewModel.onInputIntent(ChangeLocation(value = "Location A"))

        assertEquals("Location A", viewModel.inputState.value.location)
    }

    @Test
    fun `onInputIntent   ChangeCategory`() = runTest {
        // Verify that changing the category updates the state, clears previous item selections, and triggers a repository call to fetch new items.
        val categoryId = 10
        val categoryName = Category.SUB_MATERIAL.displayName

        val fakeItemType = listOf(
            ItemTypeMasterModel(
                itemTypeId = 1,
                itemCountUnitId = 1,
                itemWeightUnitId = 1,
                itemCategoryId = categoryId,
                itemTypeCode = "IT-01",
                itemTypeName = "Item name 1",
                packingType = PackingType.PAPER_BAG_25KG.displayName,
                grade = "grade",
                specificGravity = "specificGravity"
            )
        )

        coEvery { itemTypeRepo.getItemTypeByCategoryId(categoryId) } returns fakeItemType

        viewModel.onInputIntent(ChangeCategory(categoryId = categoryId, value = categoryName))
        advanceUntilIdle()

        assertEquals(categoryName, viewModel.inputState.value.category)
        assertEquals("", viewModel.inputState.value.itemInCategory)
        assertEquals(fakeItemType, viewModel.searchResults.value)
        assertTrue(viewModel.inboundInputFormResults.value.isEmpty())

    }

    @Test
    fun `onInputIntent   ChangeCategory with ID 0`() {
        // Verify that changing category with ID 0 clears `searchResults`, `inputState.category`, `itemInCategory`, and `inboundInputFormResults`.
        // TODO implement test
    }

    @Test
    fun `onInputIntent   ChangeItemInCategory`() {
        // Verify that this intent updates the state and triggers a repository call to get the form fields for the selected item.
        // TODO implement test
    }

    @Test
    fun `onInputIntent   ChangeItemInCategory with no fields`() {
        // Verify that if an item has no associated fields, `inboundInputFormResults` remains empty.
        // TODO implement test
    }

    @Test
    fun `onInputIntent   BulkApplyProcessMethod`() {
        // Test that the selected process method is applied to all checked tags in `perTagProcessMethod`.
        // TODO implement test
    }

    @Test
    fun `onInputIntent   UpdateFieldErrors`() {
        // Verify that field errors are correctly updated in `inputState` and a toast message is emitted.
        // TODO implement test
    }

    @Test
    fun `onGeneralIntent   ChangeTab`() {
        // Verify that the `tab` property in `generalState` is updated correctly.
        // TODO implement test
    }

    @Test
    fun `onGeneralIntent   ToggleFoundTag add new tag`() {
        // Verify that a new tag is added to the `foundTags` list in `generalState`.
        // TODO implement test
    }

    @Test
    fun `onGeneralIntent   ToggleFoundTag remove existing tag`() {
        // Verify that an existing tag is removed from the `foundTags` list in `generalState`.
        // TODO implement test
    }

    @Test
    fun `onGeneralIntent   ClearFoundTag`() {
        // Verify that the `foundTags` list in `generalState` is cleared.
        // TODO implement test
    }

    @Test
    fun `onGeneralIntent   Next navigation`() {
        // Verify `currentIndex` in `generalState` is incremented, but not beyond the last item index.
        // TODO implement test
    }

    @Test
    fun `onGeneralIntent   Prev navigation`() {
        // Verify `currentIndex` in `generalState` is decremented, but not below 0.
        // TODO implement test
    }

    @Test
    fun `onGeneralIntent   ChangePerTagProcessMethod`() {
        // Verify that the process method for a specific tag is updated or added in `perTagProcessMethod`.
        // TODO implement test
    }

    @Test
    fun `onGeneralIntent   ResetState`() {
        // Verify that all relevant states (`inputState`, `expandState`, `generalState`, etc.) are reset to their default values.
        // TODO implement test
    }

    @Test
    fun `onGeneralIntent   ToggleDialog`() {
        // Test that `showAppDialog` value is correctly flipped on each call.
        // TODO implement test
    }

    @Test
    fun `onGeneralIntent   ShowDialog Confirm`() {
        // Verify that a `Confirm` dialog state is set with the correct message.
        // TODO implement test
    }

    @Test
    fun `onGeneralIntent   ShowDialog Error`() {
        // Verify that an `Error` dialog state is set with the correct message.
        // TODO implement test
    }

    @Test
    fun `onGeneralIntent   HiddenDialog`() {
        // Verify that the `dialogState` is set to `Hidden`.
        // TODO implement test
    }

    @Test
    fun `onGeneralIntent   MarkOutboundProcessError`() {
        // Verify that the EPCs are correctly added to the `outboundProcessErrorSet`.
        // TODO implement test
    }

    @Test
    fun `onGeneralIntent   ClearOutboundProcessError`() {
        // Verify that `outboundProcessErrorSet` is cleared.
        // TODO implement test
    }

    @Test
    fun `onGeneralIntent   FindItemNameByKeyWord with keyword`() {
        // Verify `searchResults` are filtered correctly based on a non-blank keyword, testing case-insensitivity.
        // TODO implement test
    }

    @Test
    fun `onGeneralIntent   FindItemNameByKeyWord with blank keyword`() {
        // Verify that if the keyword is blank, the full item list for the category is reloaded from the repository.
        // TODO implement test
    }

    @Test
    fun `onExpandIntent   Toggle various expandables`() {
        // Verify that intents like `ToggleLocationExpanded`, `ToggleWinderExpanded`, etc., correctly flip the boolean value in `expandState`.
        // TODO implement test
    }

    @Test
    fun `onExpandIntent   TogglePerTagProcessExpanded`() {
        // Verify that the expanded state for a specific tag is correctly toggled in the `perTagExpanded` map.
        // TODO implement test
    }

    @Test
    fun `onExpandIntent   CloseProcessExpanded`() {
        // Verify that the expanded state for a specific tag is set to `false`.
        // TODO implement test
    }

    @Test
    fun `saveScanResultToCsv with empty data`() {
        // Test that calling with an empty data list immediately returns a failure, sets `isFileWorking` to false, and shows an error dialog.
        // TODO implement test
    }

    @Test
    fun `saveScanResultToCsv with successful save`() {
        // Mock a successful CSV save and history insertion.
        // Verify that `isFileWorking` is handled correctly, progress is updated, and a success result is returned.
        // TODO implement test
    }

    @Test
    fun `saveScanResultToCsv with failed CSV save`() {
        // Mock a `ProcessResult.Failure` from `csvHelper`.
        // Verify a failure result is returned and the correct error message is logged to history.
        // TODO implement test
    }

    @Test
    fun `saveScanResultToCsv with failed history insertion`() {
        // Mock a successful CSV save but a failed history insertion (repository returns <= 0).
        // Verify that the method returns a failure.
        // TODO implement test
    }

}